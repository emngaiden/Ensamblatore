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
package com.ensamblatore.core.assembler.format;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author emnga
 */
public class Sintax implements Serializable{
    
    private String original;
    private String processed;
    private boolean isProcessed;
    public boolean smartSpaceProcessing=true;
    public static final char[] SPECIALCHARACTERS=new char[]{
        '$',
        '|'
    } ;
    
    
    public Sintax(String sintax){
        this.original=sintax;
        this.isProcessed=false;
        this.processed=this.original;
    }
    
    public Sintax(Sintax sint){
        this.original=sint.original;
        this.isProcessed=sint.isProcessed;
        this.processed=sint.processed;
    }
    
    public Sintax(String sintax, boolean smartSpaceProcessing){
        this(sintax);
        this.smartSpaceProcessing=smartSpaceProcessing;
    }
    
    private String smartSplit(String toSplit, String splitToken, String replaceString){
        String[] splited=toSplit.split(splitToken);
        ArrayList<String> list=new ArrayList<>(Arrays.asList(splited));
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<list.size()-1; i++){
            sb.append(list.get(i)).append(replaceString);
        }
        sb.append(list.get(list.size()-1));
        return sb.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("Original: ").append(this.original).append("; ");
        sb.append("isProcessed: ").append(this.isProcessed).append("; ");
        sb.append("Processed: ").append(this.processed);
        return sb.toString();
    }
    
    public String getRegex(){
        return this.isProcessed?this.processed:this.original;
    }
    
    public void process(){
        this.processSpaces();
        this.processDots();
        this.isProcessed=true;
    }
    
    private void processSpaces(){
        String processed2=smartSplit(this.processed, " ", "(\\s)");
        if(this.smartSpaceProcessing){
            while(processed2.contains("(\\s)(\\s)")){
                processed2=processed2.replace("(\\s)(\\s)", "(\\s+)");
            }
            while(processed2.contains("(\\s+)(\\s)")){
                processed2=processed2.replace("(\\s+)(\\s)", "(\\s+)");
            }
            while(processed2.contains("(\\s+)(\\s+)")){
                processed2=processed2.replace("(\\s+)(\\s+)", "(\\s+)");
            }
            this.processed=processed2;
        }
    }
    
    private void processDots() {
        String processed2=this.smartSplit(this.processed, "\\.", "(\\.)");
        this.processed=processed2;
    }
    
    public static String fixCharacterScapes(String toFix) {
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<toFix.length(); i++){
            char found=toFix.charAt(i);
            for (char ch: SPECIALCHARACTERS){
                if(ch==found){
                    if(toFix.charAt(i-1)!='\\'){
                        sb.append('\\');
                    }
                }
            }
            sb.append(found);
        }
        return sb.toString();
    }
    
    public static String scapeAllParenthesis(String toFix){
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<toFix.length(); i++){
            char found=toFix.charAt(i);
                if(found=='(' || found==')' || found=='[' || found==']' || found=='{' || found=='}'){
                    if(toFix.charAt(i-1)!='\\'){
                        sb.append('\\');
                    }
                }
            sb.append(found);
        }
        return sb.toString();
    }

    public String getOriginal() {
        return original;
    }

    public String getProcessed() {
        return processed;
    }

    public boolean isIsProcessed() {
        return isProcessed;
    }
    
    
}
