/*
 * Copyright (C) 2017 emnga
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.ensamblatore.core.fileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emnga
 */
public class Serializer {

    public static final String CPU_EXTENSION = "encpu";
    public static final String ARCHITECTURE_EXTENSION = "enarc";
    public static final String ASSEMBLER_EXTENSION = "enass";
    public static final String TEXT_EXTENSION = "txt";
    public static final String PROGRAM_EXTENSION = "enpro";
    public static final String[] SUPPORTED_FILES = new String[]{
        CPU_EXTENSION,
        ARCHITECTURE_EXTENSION,
        ASSEMBLER_EXTENSION,
        TEXT_EXTENSION,
        PROGRAM_EXTENSION};

    public File writeObjectFile(String filename, Object write) {
        try {
            FileOutputStream fout = null;
            ObjectOutputStream oos = null;
            fout = new FileOutputStream(filename);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(write);
            System.out.println("Done");
        } catch (Exception ex) {
            Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new File(filename);
    }

    public Object readObjectFile(File file) {
        Object ret = null;
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ret = in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception ex) {

        }
        return ret;
    }

    public File writeTextFile(String filename, String toWrite) {
        BufferedWriter writer = null;
        File fil = null;
        try {
            fil = new File(filename);
            writer = new BufferedWriter(new FileWriter(fil));
            writer.write(toWrite);
            writer.close();

        } catch (Exception e) {

        }
        return fil;
    }

}
