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

package FileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class DB_Tokenizer {
    private static int n;
    private static int m;
    public static String[] tokenizeString(String db){
        return db.split("\\r?\\n");
    }
    
    public static String[] tokenizeStringWithoutLoss(String db){
        String[] ret;
        ret=db.split("\\n");
        for(int i=0; i<ret.length; i++){
            ret[i]+="\n";
        }
        return ret;
    }
    
    public static String[][] tokenizeDB(boolean check,String[] DB, String symbol){
        if(check)
            check(DB, symbol);
        n=DB[0].split(symbol).length;
        m=DB.length;
        String[][] tokenized=new String[m][n];
        for(int i=0; i<m; i++){
            tokenized[i]=DB[i].split(symbol);
        }
        return tokenized;
    }
    
    public static String[][] tokenizeDBWithoutLoss(boolean check,String[] DB, String symbol){
        if(check)
            check(DB, symbol);
        n=DB[0].split(symbol).length;
        m=DB.length;
        String[][] tokenized=new String[m][n];
        for(int i=0; i<m; i++){
            tokenized[i]=DB[i].split(symbol);
            for(int j=0; j<tokenized[i].length; j++){
                tokenized[i][j]+=symbol;
            }
        }
        return tokenized;
    }
    
    public static void check(String[] DB, String symbol){
         for(int i=0;i<DB.length-1;i++){
            if(DB[i].split(symbol).length!=DB[i+1].split(symbol).length){
                throw new IllegalStateException("Line "+(i+1)+" or "+(i+2)+" of data have different length");
            }
        }
        System.out.println(":)");
    }
    public static String fileToString(File archivo){
        String aux="";
        try (InputStream in = Files.newInputStream(archivo.toPath());
        BufferedReader reader=new BufferedReader(new InputStreamReader(in))){
        String line;
        while ((line=reader.readLine()) != null) {
            aux+=line+"\n";
        }
        } catch (IOException ex) {
           System.out.println(ex);
        }
        return aux;
    }
    
    public static String mergeArray(String... arrays){
        StringBuilder sb=new StringBuilder();
        for(String str: arrays)
            sb.append(str);
        return sb.toString();
    }
    
    public static String[][] fullSplit(File file,boolean check,String symbol){
        return tokenizeDB(check, tokenizeString(fileToString(file)), symbol);
    }
    
    public static String[][] replaceWith(String[][] data, String toReplace, String newValue){
        String[][] dat=data.clone();
        for(int i=0; i<dat.length; i++){
            for(int j=0; j<dat[0].length; j++){
                if(dat[i][j].equals(toReplace))
                    dat[i][j]=newValue;
            }
        }
        return data;
    }
}
