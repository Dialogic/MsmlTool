/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dialogic.msmltool;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.AllowHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.SupportedHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * The call class maintaining the call functionalities.
 *
 * @author ssatyana
 */
public class Call {

    static final Logger logger = Logger.getLogger(Call.class.getName());

    Dialog dialog;
    private ServerTransaction servertransaction;
    private ClientTransaction clienttransaction;
    private Request inviteRequest = null;
    public String sdp;
    public String remoteTag;
    String content;
    String callId;
    Connector sipConnector;
    private String fromAddr;
    private String fromUser;
    private String toAddr;
    private String toUser;

    public Call() {
        this.dialog = null;
        this.servertransaction = null;
        this.clienttransaction = null;
        this.sdp = null;
        this.inviteRequest = null;
        this.content = null;
        this.callId = null;
        this.sipConnector = Connector.getInstance();
    }

    /**
     * @return the tag.
     */
    private String tagGenerator() {
        return Integer.toHexString(new Random().nextInt(0xffffff) + 0xffffff);
    }

    /**
     * @param d, the dialog.
     */
    public void setDialog(Dialog d) {
        this.dialog = d;
    }

    /**
     * @return the dialog.
     */
    public Dialog getDialog() {
        return this.dialog;
    }

    /**
     * @param callId
     */
    public void setCallId(String callId) {
        this.callId = callId;
    }

    /**
     * @return the callID.
     */
    public String getCallId() {
        return this.callId;
    }

    /**
     * @return the server transaction.
     */
    public ServerTransaction getServerTransaction() {
        return this.servertransaction;
    }

    /**
     * @param s, the server transaction.
     */
    public void setServerTransaction(ServerTransaction s) {
        this.servertransaction = s;
    }

    /**
     * @return the client transaction.
     */
    public ClientTransaction getClientTransaction() {
        return this.clienttransaction;
    }

    /**
     * @param c, the client transaction.
     */
    public void setClientTransaction(ClientTransaction c) {
        this.clienttransaction = c;
    }

    /**
     * @param r, the invite request.
     */
    public void setInviteRequest(Request r) {
        this.inviteRequest = r;
    }

    /**
     * @return the inviteRequest.
     */
    public Request getInviteRequest() {
        return this.inviteRequest;
    }

    /**
     * @return the sdp.
     */
    public String getSdp() {
        return this.sdp;
    }

    /**
     * @param s. The SDP content.
     */
    public void setSdp(String s) {
        sdp = s;
    }

    /**
     * @return the remoteTag
     */
    public String getRemoteTag() {
        return this.remoteTag;
    }

    /**
     * @param tag
     */
    public void setRemoteTag(String tag) {
        remoteTag = tag;
    }

    /**
     * @return the fromAddr
     */
    public String getFromAddr() {
        return this.fromAddr;
    }

    /**
     * @param fromAddr the fromAddr to set
     */
    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    /**
     * @return the fromUser
     */
    public String getFromUser() {
        return this.fromUser;
    }

    /**
     * @param fromUser the fromUser to set
     */
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * @return the toAddr
     */
    public String getToAddr() {
        return this.toAddr;
    }

    /**
     * @param toAddr the toAddr to set
     */
    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    /**
     * @return the toUser
     */
    public String getToUser() {
        return this.toUser;
    }

    /**
     * @param toUser the toUser to set
     */
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    /**
     * This method handles the requests for the call.
     *
     * @param requestEvent.
     */
    public void handleStackRequest(RequestEvent requestEvent) {
        Request request = requestEvent.getRequest();
        switch (request.getMethod()) {
            case Request.INVITE:
                break;
            case Request.OPTIONS:
                App.optionsRequest(request, this);
                break;
            case Request.INFO:
                App.infoRequest(request);
                break;
            case Request.ACK:
                App.recievedRequest(request, this);
                break;
            case Request.CANCEL:
                App.cancelRequest(request, this);
                break;
            case Request.BYE:
                App.recievedRequest(request, this);
                break;
        }
    }

    /**
     * This method handles all the responses for the call.
     *
     * @param response. The response received.
     * @param cSeq. The cSeq to get the type of method.
     * @param dialog. The dialog related to this response.
     * @param inboundCall. The call object created by the connector for an
     * incoming call(bridge mode). This is required to handle the
     * request/response on the App.
     */
    public void handleStackResponse(Response response, CSeqHeader cSeq, Dialog dialog, Call inboundCall) {
        switch (response.getStatusCode()) {
            case Response.OK:
                switch (cSeq.getMethod()) {
                    case Request.INVITE:
                        App.sipCallResponse(response, cSeq, inboundCall);
                        break;
                    case Request.INFO:
                        break;
                    case Request.BYE:
                        App.sipCallResponse(response, cSeq, inboundCall);
                        break;
                }
                break;
            case Response.TRYING:
                App.sipCallResponse(response, cSeq, inboundCall);
                break;
            case Response.RINGING:
                App.sipCallResponse(response, cSeq, inboundCall);
                break;
            case Response.ACCEPTED:
                break;
            case Response.REQUEST_TERMINATED:
                App.requestTerminated(response, inboundCall);
                break;
        }
    }

    /**
     * This method creates an invite request with necessary headers.
     *
     * @throws java.text.ParseException
     * @throws javax.sip.InvalidArgumentException
     * @throws javax.sip.PeerUnavailableException
     */
    public void createInviteRequest() throws ParseException, InvalidArgumentException, PeerUnavailableException {
        AddressFactory addressFactory = sipConnector.getAddressFactory();
        HeaderFactory headerFactory = sipConnector.getHeaderFactory();

        SipURI requestUri = addressFactory.createSipURI(this.getToUser(), this.getToAddr());

        SipURI fromUri = addressFactory.createSipURI(this.getFromUser(), this.getFromAddr());
        Address fromAddress = addressFactory.createAddress(fromUri);
        FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, tagGenerator());

        SipURI toUri = addressFactory.createSipURI(this.getToUser(), this.getToAddr());
        Address toAddress = addressFactory.createAddress(toUri);
        ToHeader toHeader = headerFactory.createToHeader(toAddress, null);

        ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
        ViaHeader viaHeader = headerFactory.createViaHeader(this.getFromAddr(), sipConnector.sipProvider.getListeningPoint("udp").getPort(), "udp", null);
        viaHeaders.add(viaHeader);

        CallIdHeader callIdHeader = sipConnector.sipProvider.getNewCallId();
        this.setCallId(callIdHeader.getCallId());
        CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L, Request.INVITE);

        ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "sdp");

        MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);

        /**
         * WITHOUT SDP
         * <p>
         * Request request =
         * sipConnector.getMessageFactory().createRequest(requestUri,
         * Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader,
         * viaHeaders, maxForwardsHeader);
         * </p>
         */
        //WITH SDP
        Request request = sipConnector.getMessageFactory().createRequest(requestUri, Request.INVITE,
                callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwardsHeader, contentTypeHeader, this.getSdp().getBytes());

        /**
         * Add support for session timers - feature
         * <p>
         * Header supportedHeader = headerFactory.createHeader("Supported",
         * "timer"); Header requireHeader =
         * headerFactory.createHeader("Require", "timer"); Header sessionExpires
         * = headerFactory.createHeader("Session-Expires", "90;refresher=uas");
         * Header minSE = headerFactory.createHeader("Min-SE", "90");
         * request.addHeader(supportedHeader); request.addHeader(requireHeader);
         * request.addHeader(sessionExpires); request.addHeader(minSE);
         * </p>
         */
        SipURI contactUri = addressFactory.createSipURI(this.getFromUser(), this.getFromAddr());
        contactUri.setPort(sipConnector.sipProvider.getListeningPoint("udp").getPort());
        Address contactAddress = addressFactory.createAddress(contactUri);
        ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
        request.addHeader(contactHeader);

        sipConnector.register(this);
        System.out.println("CREATING AN INVITE REQUEST TO XMS");
        App.updateCallTextBridgeRequestToXMS(request);
        sipConnector.sendRequest(request, this);

    }

    /**
     * Creates an ACK request.
     *
     * @param response
     */
    public void createAckRequest(Response response) {
        System.out.println("CREATING ACK REQUEST ");
        Request ackRequest;
        try {
            ackRequest = this.getDialog().createAck(((CSeqHeader) response.getHeader(CSeqHeader.NAME)).getSeqNumber());
            App.updateCallTextBridgeRequestToXMS(ackRequest);
            sipConnector.sendAck(ackRequest, dialog);
        } catch (InvalidArgumentException | SipException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates an INFO request.
     *
     * @param msml
     */
    public void createInfoRequest(String msml) {
        HeaderFactory headerFactory = sipConnector.getHeaderFactory();
        try {
            Request infoRequest = this.getDialog().createRequest(Request.INFO);
            ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "xml");
            infoRequest.addHeader(contentTypeHeader);

            /**
             * Custom MSML.
             * <p>
             * String msmlContent =
             * "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" // +
             * "<msml version=\"1.1\">\n" // +
             * "<dialogstart target=\"conn:" + call.getDialog().getRemoteTag() + "\" type=\"application/moml+xml\" name=\"DIALOG:AudioPlay\">\n"
             * // + "	<play >\n" // + "
             * <audio uri=\"file://verification/greeting.wav\" />\n" // + "
             * </play>\n" // + "</dialogstart>\n" // + "</msml>";
             * </p>
             * call.setContent(msmlContent);
             */
            System.out.println("REMOTE TAG -> " + this.getDialog().getRemoteTag());
            msml = msml.replaceAll("conn:.*?\\\"", "conn:" + this.getDialog().getRemoteTag() + "\"");
            infoRequest.setContent(msml, contentTypeHeader);
            System.out.println("CREATING INFO REQUEST TO XMS");
            sipConnector.sendRequest(infoRequest, this);

        } catch (SipException | ParseException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates an INFO response.
     *
     * @param request
     */
    public void createInfoResponse(Request request) {
        MessageFactory messageFactory = sipConnector.getMessageFactory();
        HeaderFactory headerFactory = sipConnector.getHeaderFactory();
        AddressFactory addressFactory = sipConnector.getAddressFactory();
        try {
            Response infoOkResponse = messageFactory.createResponse(Response.OK, request);
            SipURI contactUri = addressFactory.createSipURI(this.getFromUser(), this.getFromAddr());
            contactUri.setPort(sipConnector.sipProvider.getListeningPoint("udp").getPort());
            Address contactAddress = addressFactory.createAddress(contactUri);
            ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
            infoOkResponse.addHeader(contactHeader);
            System.out.println("CREATING INFO RESPONSE TO XMS");
            sipConnector.sendResponse(infoOkResponse, this);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates an OPTIONS response.
     *
     * @param request
     */
    public void createOptionsResponse(Request request) {
        System.out.println("CREATING OPTIONS RESPONSE");
        try {
            MessageFactory messageFactory = sipConnector.getMessageFactory();
            Response optionsResponse = messageFactory.createResponse(Response.OK, request);
            ToHeader toHeader = (ToHeader) optionsResponse.getHeader(ToHeader.NAME);
            toHeader.setTag(Integer.toHexString(new Random().nextInt(0xffffff) + 0xffffff));
            App.updateCallTextBridgeSentResponse(optionsResponse);
            sipConnector.sendResponse(optionsResponse, this);
            //this.setSdp(new String(request.getRawContent()));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    /**
     * Creates a TRYING response.
     *
     * @param request
     */
    public void createTryingResponse(Request request) {
        System.out.println("CREATING 100 TRYING RESPONSE");
        try {
            MessageFactory messageFactory = sipConnector.getMessageFactory();
            Response tryingResponse = messageFactory.createResponse(Response.TRYING, request);
            ToHeader toHeader = (ToHeader) tryingResponse.getHeader(ToHeader.NAME);
            toHeader.setTag(Integer.toHexString(new Random().nextInt(0xffffff) + 0xffffff));
            App.updateCallTextBridgeSentResponse(tryingResponse);
            sipConnector.sendResponse(tryingResponse, this);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates a RINGING response.
     *
     * @param request
     */
    public void createRingingResponse(Request request) {
        System.out.println("CREATING 180 RINGING RESPONSE");
        try {
            MessageFactory messageFactory = sipConnector.getMessageFactory();
            Response ringingResponse = messageFactory.createResponse(Response.RINGING, request);
            ToHeader toHeader = (ToHeader) ringingResponse.getHeader(ToHeader.NAME);
            toHeader.setTag(Integer.toHexString(new Random().nextInt(0xffffff) + 0xffffff));
            App.updateCallTextBridgeSentResponse(ringingResponse);
            sipConnector.sendResponse(ringingResponse, this);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates an INVITE OK response.
     *
     * @param request
     */
    public void createInviteOk(Request request) {
        System.out.println("CREATING 200OK FOR INVITE");
        MessageFactory messageFactory = sipConnector.getMessageFactory();
        AddressFactory addressFactory = sipConnector.getAddressFactory();
        HeaderFactory headerFactory = sipConnector.getHeaderFactory();

        ToHeader toHeader = (ToHeader) request.getHeader("To");
        Address reqToAddress = toHeader.getAddress();
        try {
            Response okResponse = messageFactory.createResponse(Response.OK, this.getServerTransaction().getRequest());

            Address contactAddress = addressFactory.createAddress(reqToAddress.toString());
            ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
            okResponse.addHeader(contactHeader);
            MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
            okResponse.addHeader(maxForwardsHeader);

            AllowHeader allowHeader = headerFactory.createAllowHeader("INVITE, BYE, ACK, CANCEL, OPTIONS, INFO");
            okResponse.addHeader(allowHeader);
            ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "sdp");

            okResponse.setContent(this.getSdp(), contentTypeHeader);
            App.updateCallTextBridgeSentResponse(okResponse);
            sipConnector.sendResponse(okResponse, this);
        } catch (ParseException | InvalidArgumentException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Creates a BYE request.
     */
    public void createBye() {
        try {
            Request byeRequest = this.getDialog().createRequest(Request.BYE);
            System.out.println("CREATE BYE REQUEST ->" + byeRequest);
            sipConnector.sendRequest(byeRequest, this);
        } catch (SipException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates a BYE OK response.
     *
     * @param request
     */
    public void doByeOk(Request request) {
        MessageFactory messageFactory = sipConnector.getMessageFactory();
        try {
            Response okResponse = messageFactory.createResponse(Response.OK, request);
            sipConnector.sendResponse(okResponse, this);
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void doReInviteOk(Call call, Request request) {
        MessageFactory messageFactory = sipConnector.getMessageFactory();
        AddressFactory addressFactory = sipConnector.getAddressFactory();
        HeaderFactory headerFactory = sipConnector.getHeaderFactory();

        ToHeader toHeader = (ToHeader) request.getHeader("To");
        Address reqToAddress = toHeader.getAddress();
        try {
            Response okResponse = messageFactory.createResponse(Response.OK, request);

            SupportedHeader supportedHeader = headerFactory.createSupportedHeader("timer");
            okResponse.addHeader(supportedHeader);
            Header sessionExpiresHeader = request.getHeader("Session-Expires");
            okResponse.addHeader(sessionExpiresHeader);
            Address contactAddress = addressFactory.createAddress(reqToAddress.toString());
            ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
            okResponse.addHeader(contactHeader);

            MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
            okResponse.addHeader(maxForwardsHeader);

            AllowHeader allowHeader = headerFactory.createAllowHeader("INVITE, BYE, ACK, CANCEL, OPTIONS, INFO");
            okResponse.addHeader(allowHeader);
            ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "sdp");

            okResponse.setContent(request.getContent(), contentTypeHeader);
            ResponseEvent responseEvent = new ResponseEvent(sipConnector.sipProvider, null, call.getServerTransaction().getDialog(), okResponse);
            sipConnector.processResponse(responseEvent);
        } catch (ParseException | InvalidArgumentException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates a CANCEL request.
     */
    public void createCancelRequest() {
        System.out.println("CREATING CANCEL REQUEST");
        try {
            Request cancelRequest = this.getClientTransaction().createCancel();
            sipConnector.sendRequest(cancelRequest, this);
        } catch (SipException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates a CANCEL response.
     *
     * @param request
     */
    public void createCancelResponse(Request request) {
        MessageFactory messageFactory = sipConnector.getMessageFactory();
        Response okResponse;
        try {
            okResponse = messageFactory.createResponse(Response.OK, request);
            sipConnector.sendResponse(okResponse, this);
            // send request terminate
            //Response requestTerminatedResponse = messageFactory.createResponse(Response.REQUEST_TERMINATED, call.getInviteRequest());
            //sipConnector.sendResponse(requestTerminatedResponse, this);
        } catch (ParseException ex) {
            Logger.getLogger(Call.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a request terminated ACK.
     *
     * @param response
     */
    public void createRequestTerminated(Response response) {
        Request ackRequest;
        try {
            ackRequest = dialog.createAck(((CSeqHeader) response.getHeader(CSeqHeader.NAME)).getSeqNumber());
            sipConnector.sendTerminationAck(ackRequest, dialog);
        } catch (InvalidArgumentException | SipException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Send the INFO response to the App to display to the user.
     *
     * @param response
     */
    public void sendInfoResponse(String response) {
        App.diaplayInfoResponse(response);
    }

    /**
     * Notify the app about the incoming call.
     *
     * @param reqEvent
     * @param inboundCall
     */
    public void notifyApp(RequestEvent reqEvent, Call inboundCall) {
        App app = new App();
        Request request = reqEvent.getRequest();
        app.makeCall(request, inboundCall);
    }
}
