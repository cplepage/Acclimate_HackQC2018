package com.example.demo;

import java.io.*;

public class putAlert {

    private boolean status;

    public putAlert(String type, double[] coordinates) {
        try {
            RandomAccessFile f = new RandomAccessFile("src/main/recentAlert.json", "rw");
            long length = f.length() - 1;
            byte b = 0;
            do {
                length -= 1;
                f.seek(length);
                b = f.readByte();
            } while(b != 10);
            f.setLength(length+1);
            f.close();

            String str = "{ \"type\" : \"Feature\", \"risque\" : \"" + type + "\", \"geometry\" : { \"type\" : \"Point\", \"coordinates\" : [" + coordinates[1] + "," + coordinates[0] + "] }}";
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter("src/main/recentAlert.json", true));
            writer.append(",\n");
            writer.append(str);
            writer.append("\n]");

            writer.close();
            this.status = true;
        } catch (IOException e) {
            e.printStackTrace();
            this.status = false;
        }

    }

    public boolean isStatus() {
        return status;
    }

}
