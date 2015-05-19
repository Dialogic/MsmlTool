/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dialogic.msmltool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/**
 *
 * @author ssatyana
 */
public class ReadFileUtility {

    private static List<String> lines = new ArrayList<>();

    /**
     * Reads the contents of the file which has the user input. The file
     * consists of the XMS IP address and the local port.
     *
     * @return list of string which holds the contents of the file.
     */
    public static List<String> readFile() {
        File file = new File("IpAddress.txt");
        Scanner scan = null;
        try {
            if (file.exists()) {
                scan = new Scanner(file);
                while (scan.hasNextLine()) {
                    lines.add(scan.nextLine());
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XMSForm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (scan != null) {
                scan.close();
            }
        }
        return lines;
    }

    /**
     * Reads the contents of the Connector config file.
     *
     * @return the local port.
     */
    public static Integer getPortFromConfigFile() {
        // get the port information from the config file.
        int port = 0;
        FileInputStream xmlFile;
        try {
            xmlFile = new FileInputStream("ConnectorConfig.xml");
            Document doc = new Builder().build(xmlFile);
            Element root = doc.getRootElement();
            Elements entries = root.getChildElements();
            for (int x = 0; x < entries.size(); x++) {
                Element element = entries.get(x);
                if (element.getLocalName().equals("port")) {
                    System.out.println("PORT FROM THE CONFIG FILE -> " + element.getValue());
                    port = Integer.parseInt(element.getValue());
                }
            }
        } catch (ParsingException | IOException | NumberFormatException ex) {
            Logger.getLogger(ReadFileUtility.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return port;
    }

    /**
     * Used by the connector to get the user inputs.
     *
     * @return the lines
     */
    public static List<String> getLines() {
        return lines;
    }

    /**
     * @param data
     */
    public static void setLines(List<String> data) {
        lines = data;
    }
}
