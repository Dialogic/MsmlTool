/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dialogic.msmltool;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.header.CSeqHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * Holds the business logic.
 *
 * @author ssatyana
 */
public class App {

    static final Logger logger = Logger.getLogger(App.class.getName());

    static String toUser;
    static String toAdr;
    static Call callObject;
    static CallUIForm callForm;
    static Call call;
    static int counter = 0;
    static boolean buttonsActive = true;
    static boolean hangup = false;
    static boolean xmsBye = false;
    static Call inboundCall;

    /**
     * Starting point of the application. Initializes a JFrame for the user to
     * enter the XMS information.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        XMSForm.initialize();

        /**
         * prints out the call flow to output.txt file
         * <p>
         * System.setOut( new PrintStream(new BufferedOutputStream(new
         * FileOutputStream("output.txt")), true));
         * </p>
         */
    }

    /**
     * Initializes a JFrame for the user to make call, hangup call, exchange
     * MSML, etc.
     */
    public void createCallForm() {
        callForm = CallUIForm.initialize();
        Connector.getInstance();
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
            call = new Call();
            call.setToAddr(addr);
            call.setToUser(user);
            call.setFromAddr(Inet4Address.getLocalHost().getHostAddress());
            call.setFromUser("MsmlTool");
            call.setSdp(sdp);
            if (call.getSdp() == null) {
                call.setSdp(getStringMessage());
            }
            call.createInviteRequest();
        } catch (UnknownHostException | ParseException | InvalidArgumentException | PeerUnavailableException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates a call to XMS for an incoming request.
     *
     * @param request
     * @param inboundCall
     */
    public void makeCall(Request request, Call inboundCall) {
        try {
            inboundCall.setFromAddr(Inet4Address.getLocalHost().getHostAddress());
            inboundCall.setFromUser("MsmlTool");
            buttonsActive = callForm.disableButtons();
            // this is used to get the inboundCall to create bye when close button is clicked
            setInboundCall(inboundCall);
            callForm.updateCallTextAreaBridgeMode(request, null);
            makeCall(callForm.getUserTextFieldValue(), getXMSAdr(), new String(request.getRawContent()));
        } catch (UnknownHostException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Logic to handle responses.
     *
     * @param response
     * @param cSeq
     * @param inboundCall
     */
    public static void sipCallResponse(Response response, CSeqHeader cSeq, Call inboundCall) {
        switch (response.getStatusCode()) {
            case Response.OK:
                switch (cSeq.getMethod()) {
                    case Request.INVITE:
                        if (buttonsActive) {
                            callForm.updateCallTextArea(response);
                            call.createAckRequest(response);
                        } else {
                            callForm.updateCallTextAreaBridgeXMS(response, null);
                            call.createAckRequest(response);
                            inboundCall.createInviteOk(inboundCall.getInviteRequest());
                        }
                        break;
                    case Request.INFO:
                        break;
                    case Request.BYE:
                        if (buttonsActive) {
                            callForm.updateCallTextArea(response);
                        } else {
                            callForm.setVisible(false);
                            System.exit(0);
                        }
                        break;
                }
                break;
            case Response.TRYING:
                if (buttonsActive) {
                    callForm.updateCallTextArea(response);
                } else {
                    callForm.updateCallTextAreaBridgeXMS(response, null);
                    inboundCall.createTryingResponse(inboundCall.getInviteRequest());
                }
                break;
            case Response.RINGING:
                if (buttonsActive) {
                    callForm.updateCallTextArea(response);
                } else {
                    callForm.updateCallTextAreaBridgeXMS(response, null);
                    inboundCall.createRingingResponse(inboundCall.getInviteRequest());
                }
                break;
        }
    }

    /**
     * Logic to handle requests. Ex: ACK, BYE, etc.
     *
     * @param request
     * @param c
     */
    public static void recievedRequest(Request request, Call c) {
        switch (request.getMethod()) {
            case Request.ACK:
                callForm.updateCallTextAreaBridgeMode(request, null);
                break;
            case Request.BYE:
                if (buttonsActive) {
                    // direct mode
                    callForm.updateCallTextArea();
                    xmsBye = true;
                    call.doByeOk(request);
                } else if (c == call) {
                    // bye request sent by the XMS, bridge mode
                    xmsBye = true;
                    call.doByeOk(request);
                    getInboundCall().createBye();
                } else {
                    // bye request sent by the softphone, bridge mode
                    c.doByeOk(request);
                    call.createBye();
                }
                break;
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
            call.createInfoRequest(msml);
        }
    }

    /**
     * Displays the INFO responses to the user.
     *
     * @param msml
     */
    public static void diaplayInfoResponse(String msml) {
        callForm.updateRecievedMessage(msml);
    }

    /**
     * Send an INFO OK response for the INFO request received from XMS. Also
     * displays this INFO request to the user
     *
     * @param request
     */
    public static void infoRequest(Request request) {
        call.createInfoResponse(request);
        callForm.updateRecievedMessage(new String(request.getRawContent()));
    }

    /**
     * Send a response for the OPTIONS request.
     *
     * @param request
     * @param c. The incoming call object
     */
    public static void optionsRequest(Request request, Call c) {
        callForm.updateCallTextAreaBridgeMode(request, null);
        c.createOptionsResponse(request);
    }

    /**
     * Send a response for the CANCEL request .
     *
     * @param request
     * @param c. The incoming call object
     */
    public static void cancelRequest(Request request, Call c) {
        c.createCancelResponse(request);
        if (call != null) {
            call.createCancelRequest();
        }
    }

    /**
     * Send ACK for request terminated.
     *
     * @param response
     * @param c. The incoming call object
     */
    public static void requestTerminated(Response response, Call c) {
        call.createRequestTerminated(response);
    }

    /**
     * Send BYE request when the user clicks on the hangup button.
     */
    public void hangup() {
        call.createBye();
        hangup = true;
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
            Call callForIncoming = getInboundCall();
            if (callForIncoming != null && callForIncoming.getDialog() != null) {
                callForIncoming.createBye();
            }
            System.exit(0);
        }
    }

    /**
     *
     * @param adr.
     */
    public static void setXMSAdr(String adr) {
        toAdr = adr;
    }

    /**
     *
     * @return the XMS IP address.
     */
    public static String getXMSAdr() {
        return toAdr;
    }

    /**
     * Used to create BYE when close button is clicked.
     *
     * @param c
     */
    public static void setInboundCall(Call c) {
        inboundCall = c;
    }

    /**
     * Used to create BYE when close button is clicked.
     *
     * @return the inbound call object.
     */
    public static Call getInboundCall() {
        return inboundCall;
    }

    /**
     * Returns a dummy SDP.
     *
     * @return contentString. The SDP for the INVITE request.
     */
    public String getStringMessage() {
        String contentString = "v=0\r\n"
                + "o=swetha 575 654 IN IP4 10.20.120.24\r\n"
                + "s=Talk\r\n"
                + "c=IN IP4 10.20.120.24\r\n"
                + "t=0 0\r\n"
                + "m=audio 7078 RTP/AVP 0 8 18 101\r\n"
                + "a=rtpmap:0 PCMU/8000\r\n"
                + "a=rtpmap:101 telephone-event/8000\r\n"
                + "a=fmtp:101 0-11\r\n"
                + "a=sendrecv\r\n\r\n";
        return contentString;
    }

    /**
     * This method is used to let the user know about the call flow via the UI.
     * Displays the responses sent to an incoming invite request. Ex: 200OK,
     * 100, 180, etc.
     *
     * @param res
     */
    public static void updateCallTextBridgeSentResponse(Response res) {
        callForm.updateCallTextAreaBridgeMode(null, res);
    }

    /**
     * This method is used to let the user know about the call flow via the UI.
     * Displays the requests sent to the XMS, ex: INVITE, ACK, etc.
     *
     * @param req
     */
    public static void updateCallTextBridgeRequestToXMS(Request req) {
        callForm.updateCallTextAreaBridgeXMS(null, req);
    }
}
