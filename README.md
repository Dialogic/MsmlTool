![alt tag](https://www.dialogic.com/~/media/shared/graphics/video/nwrkfuel-posterimg.jpg)

Dialogic PowerMedia XMS
=======================
Dialogic’s PowerMedia™ XMS is a powerful next-generation software media server that enables standards-based, real-time multimedia communications solutions for mobile and broadband environments. PowerMedia XMS supports standard media control interfaces such as MSML, VXML, NetAnn, and JSR 309, plus a Dialogic HTTP-based version of a RESTful API.

MSMLTool
================
Overview: MsmlTool is a JAVA based 3pcc test application which can be used for direct media-less calls to XMS or to make a bridged call using linphone. Along with call setup it can also be used for passing and receiving MSML messages to and from the Powermedia XMS. 

Repository Contents
===================
Sample MsmlTool for the Dialogic eXtended Media Sever (XMS) platform.  This is used by the services team for issue reproduction and rapid demo prototyping.

The repository consists of 3 branches

1. master - Refer to MsmlToolGuide under /docs to understand the different modes and how to use the tool to test with Powermedia XMS
2. msmltool2.0 - This is the latest version of Msmltool which uses Client Library components for the call and connector setup. Refer to MsmlToolGuide for version 2.0 changes.
3. msmltool-rtpplay - This branch is created on top of msmltool2.0 which supports rtp-play.