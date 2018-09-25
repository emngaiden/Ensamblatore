/*
 * Copyright (C) 2018 emnga
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
package Core.Assembler.Instruction;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This clas conerts numbers between bases.
 * the minimum base is 2, and the maximum base is 36.
 * 
 * The base conversion follows the Hex conversion method and numbering.
 * In base 16, an F is equal to 15, in base 17, G will be equal to 16
 * and so on untill the Z wich will be equal to 35.
 * @author emnga
 */
public class BaseConverter {

    
    public static char[] values=new char[]{
        '0','1','2','3','4','5','6','7','8','9',
        'a','b','c','d','e','f','g','h','i','j',
        'k','l','m','n','o','p','q','r','s','t',
        'u','v','w','x','y','z'};
    public static String convert(String value, int originalBase, int newBase){
        if(originalBase==newBase)
            return value;
        ArrayList<Integer> list=new ArrayList<>();
        list.add(0);
        StringBuilder sb=new StringBuilder(value);
        String auxOriginal=sb.reverse().toString();
        int[] ori=new int[value.length()];
        for(int i=0; i<ori.length; i++){
            for(int j=0; j<values.length; j++){
                if(values[j]==auxOriginal.charAt(i)){
                    ori[i]=j;
                }
            }
        }
        int[] charaux=new int[ori.length];
        int oriPositionCounter=0;
        int newPositionCounter=0;
        while(!Arrays.equals(ori, charaux)){
            if(list.get(newPositionCounter)==newBase-1){
                try{
                while(list.get(newPositionCounter)==newBase-1){
                    list.set(newPositionCounter, 0);
                    newPositionCounter++;
                }
                }catch(IndexOutOfBoundsException e){
                    list.add(0);
                }
                list.set(newPositionCounter, list.get(newPositionCounter)+1);
                newPositionCounter=0;
            }
            else{
                list.set(newPositionCounter, list.get(newPositionCounter)+1);
            }
            if(charaux[oriPositionCounter]==originalBase-1){
                while(charaux[oriPositionCounter]==originalBase-1){
                    charaux[oriPositionCounter]=0;
                    oriPositionCounter++;
                }
                charaux[oriPositionCounter]++;
                oriPositionCounter=0;
            }
            else{
                charaux[oriPositionCounter]++;
            }
        }
        StringBuilder ret=new StringBuilder();
        list.forEach((in) -> {
            ret.append(values[in]);
        });
        return ret.reverse().toString();
    }
}
