/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dialogic.msmltool;

import com.dialogic.msml.MsmlEvent;
import com.dialogic.msml.MsmlEventType;
import com.dialogic.msml.XMSMsmlConnector;
import com.dialogic.msml.XMSSipCall;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sip.address.Address;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;

/**
 *
 * @author ssatyana
 */
public class MsmlApp implements Observer {

    static final Logger logger = Logger.getLogger(MsmlApp.class.getName());

    static String toUser;
    static String toAdr;
    static Call callObject;
    static CallUIForm callForm;
    static XMSSipCall call;
    static int counter = 0;
    static boolean buttonsActive = true;
    static boolean reinvite = false;
    static boolean hangup = false;
    static boolean xmsBye = false;
    static XMSSipCall inCall;
    static XMSMsmlConnector connector;

    /**
     * Starting point of the application. Initializes a JFrame for the user to
     * enter the XMS information.
     *
     */
    public void run() {
        XMSForm.initialize(this);
        try {
            /**
             * prints out the call flow to output.txt file
             */
            //System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt")), true));
        } catch (Exception ex) {
            Logger.getLogger(MsmlApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes a JFrame for the user to make call, hangup call, exchange
     * MSML, etc.
     */
    public void createCallForm() {
        callForm = CallUIForm.initialize(this);
        try {
            connector = new XMSMsmlConnector("ConnectorConfig.xml", Inet4Address.getLocalHost().getHostAddress(),
                    Integer.parseInt(ReadFileUtility.getValue("port")));
            XMSSipCall waitCall = new XMSSipCall(connector);
            waitCall.addObserver(this);
            waitCall.addToWaitList();

        } catch (UnknownHostException ex) {
            Logger.getLogger(MsmlApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a call.
     *
     * @param user
     * @param addr
     * @param sdp
     */
    public void makeCall(String user, String addr, String sdp) {
        try {
            call = new XMSSipCall(connector);
            call.addObserver(this);
            call.setToUser(user);
            call.setFromAddress(Inet4Address.getLocalHost().getHostAddress());
            call.setFromUser("MsmlTool");
            call.setLocalSdp(sdp);
            if (call.getLocalSdp() == null) {
                call.setLocalSdp(getStringMessage());
            }
            call.createInviteRequest(user, addr);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates a call to XMS for an incoming request.
     *
     * @param request
     * @param inboundCall
     */
    public void makeCall(Request request, XMSSipCall inboundCall) {
        try {
            inboundCall.setFromAddress(Inet4Address.getLocalHost().getHostAddress());
            inboundCall.setFromUser("MsmlTool");
            buttonsActive = callForm.disableButtons();
            setInboundCall(inboundCall);
            callForm.updateCallTextAreaBridgeMode(request, null);
            makeCall(ReadFileUtility.getValue("appid"), ReadFileUtility.getValue("baseurl"), new String(request.getRawContent()));
        } catch (UnknownHostException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Send an INFO request based on the information send by the user via the
     * UI.
     *
     * @param msml
     */
    public void sendMsml(String msml) {
        if (call != null) {
            System.out.println("Call" + call);
            call.sendInfo(msml);
        }
    }

    /**
     * Send BYE request when the user clicks on the hangup button.
     */
    public void hangup() {
        if (call != null) {
            call.createBye();
            hangup = true;
            call = null;
        }
    }

    /**
     * Send BYE request when the user clicks the close button.
     */
    public void close() {
        if (hangup) {
            System.exit(0);
        } else if (xmsBye) {
            System.exit(0);
        } else {
            if (call != null && call.getDialog() != null) {
                call.createBye();
            }
            XMSSipCall callForIncoming = getInboundCall();
            if (callForIncoming != null && callForIncoming.getDialog() != null) {
                callForIncoming.createBye();
            }
            System.exit(0);
        }
    }

    /**
     * Used to create BYE when close button is clicked.
     *
     * @param c
     */
    public static void setInboundCall(XMSSipCall c) {
        inCall = c;
    }

    /**
     * Used to create BYE when close button is clicked.
     *
     * @return the inbound call object.
     */
    public static XMSSipCall getInboundCall() {
        return inCall;
    }

    /**
     * Returns a dummy SDP.
     *
     * @return contentString. The SDP for the INVITE request.
     */
    public String getStringMessage() {
        String contentString = null;
        try {
            String localAdr = Inet4Address.getLocalHost().getHostAddress();
            contentString = "v=0\r\n"
                    + "o=MsmlTool 575 654 IN IP4 " + localAdr + "\r\n"
                    + "s=Talk\r\n"
                    + "c=IN IP4 " + localAdr + "\r\n"
                    + "t=0 0\r\n"
                    + "m=audio 7070 RTP/AVP 0 8 18 101\r\n"
                    + "a=rtpmap:0 PCMU/8000\r\n"
                    + "a=rtpmap:101 telephone-event/8000\r\n"
                    + "a=fmtp:101 0-11\r\n"
                    + "a=sendrecv\r\n\r\n";
            // example sdp with video
//            contentString = "v=0\n"
//                    + "o=MsmlTool 575 654 IN IP4 " + localAdr + "\r\n"
//                    + "s=Talk\r\n"
//                    + "c=IN IP4 " + localAdr + "\r\n"
//                    + "t=0 0\r\n"
//                    + "m=audio 7070 RTP/AVP 0 101\n"
//                    + "a=rtpmap:101 telephone-event/8000\n"
//                    + "m=video 7080 RTP/AVP 96\n"
//                    + "a=rtpmap:96 VP8/90000";
        } catch (UnknownHostException ex) {
            Logger.getLogger(MsmlApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contentString;
    }

    /**
     * This is the Notify handler that will be called by EventThread when new
     * events are created.
     *
     * @param o
     * @param o1
     */
    @Override
    public void update(Observable o, Object o1) {
        MsmlEvent e = (MsmlEvent) o1;
        if (e.getType().equals(MsmlEventType.INCOMING)) {
            if (e.getReq() != null) {
                this.makeCall(e.getReq(), e.getCall());
            }
        } else if (e.getType().equals(MsmlEventType.INVITE)) {
            if (e.getReq() != null) {
                callForm.updateCallTextAreaBridgeXMS(null, e.getReq());
            }
        } else if (e.getType().equals(MsmlEventType.REINVITE)) {
            // MRB updates
            if (e.getReq() != null) {
                callForm.updateCallTextAreaBridgeXMS(null, e.getReq());
                call.setInviteRequest(e.getReq());
                reinvite = true;
                getInboundCall().setLocalSdp(new String(e.getReq().getRawContent()));
                getInboundCall().sendReinviteRequest(e.getReq());
            }
        } else if (e.getType().equals(MsmlEventType.TRYING)) {
            if (e.getRes() != null) {
                callForm.updateCallTextAreaBridgeXMS(e.getRes(), null);
            }
        } else if (e.getType().equals(MsmlEventType.RINGING)) {
            if (e.getRes() != null) {
                callForm.updateCallTextAreaBridgeXMS(e.getRes(), null);
                if (!buttonsActive) {
                    System.out.println("tracking 180" + getInboundCall().getInviteRequest());
                    getInboundCall().createRingingResponse(getInboundCall().getInviteRequest());
                    ToHeader toHeader = (ToHeader) e.getRes().getHeader("To");
                    Address resToAddress = toHeader.getAddress();
                    FromHeader fromHeader = (FromHeader) e.getRes().getHeader("From");
                    Address resFromAddress = fromHeader.getAddress();
                    callForm.updateCallTextAreaWithCustomMessage(resFromAddress.toString(), resToAddress.toString(), "RINGING");
                }
            }
        } else if (e.getType().equals(MsmlEventType.CONNECTING)) {
            if (e.getRes() != null) {
                callForm.updateCallTextAreaBridgeXMS(e.getRes(), null);
                ToHeader toHeader = (ToHeader) e.getRes().getHeader("To");
                Address resToAddress = toHeader.getAddress();
                FromHeader fromHeader = (FromHeader) e.getRes().getHeader("From");
                Address resFromAddress = fromHeader.getAddress();
                callForm.updateCallTextAreaWithCustomMessage(resFromAddress.toString(), resToAddress.toString(), "ACK");
                if (!buttonsActive) {
                    if (reinvite) {
                        //MRB updates
                        call.setLocalSdp(new String(e.getRes().getRawContent()));
                        call.createInviteOk(call.getInviteRequest());
                    } else {
                        //e.getCall().createAckRequest(e.getRes());
                        getInboundCall().setLocalSdp(new String(e.getRes().getRawContent()));
                        getInboundCall().createInviteOk(getInboundCall().getInviteRequest());
                    }
                    ToHeader to = (ToHeader) getInboundCall().getInviteRequest().getHeader("To");
                    Address toAddress = to.getAddress();
                    FromHeader from = (FromHeader) getInboundCall().getInviteRequest().getHeader("From");
                    Address fromAddress = from.getAddress();

                    callForm.updateCallTextAreaWithCustomMessage(toAddress.toString(), fromAddress.toString(), "200OK");
                }
                String sdp = new String(e.getRes().getRawContent());
                if (sdp.contains("xmserver")) {
                    Pattern pattern = Pattern.compile("m=video (.*?) RTP");
                    Matcher m = pattern.matcher(sdp);
                    if (m.find()) {
                        callForm.setPort(m.group(1));
                    }
                    Pattern ipAdr = Pattern.compile("o=xmserver .* (.*)");
                    Matcher mIP = ipAdr.matcher(sdp);
                    if (mIP.find()) {
                        callForm.setIpAdr(mIP.group(1));
                    }
                }
            }
        } else if (e.getType().equals(MsmlEventType.CONNECTED)) {
            if (e.getReq() != null) {
                callForm.updateCallTextAreaBridgeMode(e.getReq(), null);
            }
        } else if (e.getType().equals(MsmlEventType.INFORESPONSE)) {
            if (e.getRes() != null) {
                callForm.updateRecievedMessage(new String(e.getRes().getRawContent()));
            }
        } else if (e.getType().equals(MsmlEventType.INFOREQUEST)) {
            if (e.getReq() != null) {
                e.getCall().createInfoResponse(e.getReq());
                callForm.updateRecievedMessage(new String(e.getReq().getRawContent()));
            }
        } else if (e.getType().equals(MsmlEventType.DISCONNECTED)) {
            if (e.getReq() != null) {
                callForm.updateCallTextAreaBridgeXMS(null, e.getReq());
                if (buttonsActive) {
                    // direct mode
                    callForm.updateCallTextArea();
                    xmsBye = true;
                    call.doByeOk(e.getReq());
                } else if (e.getCall() == call) {
                    // bye request sent by the XMS, bridge mode
                    xmsBye = true;
                    call.doByeOk(e.getReq());
                    getInboundCall().createBye();
                } else {
                    // bye request sent by the softphone, bridge mode
                    e.getCall().doByeOk(e.getReq());
                    call.createBye();
                }
            } else if (e.getRes() != null) {
                if (buttonsActive) {
                    callForm.updateCallTextArea(e.getRes());
                } else {
                    callForm.setVisible(false);
                    System.exit(0);
                }
            }
        }
    }
}
