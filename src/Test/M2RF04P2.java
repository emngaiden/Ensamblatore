/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Core.Assembler.Assembler;
import Core.Assembler.Instruction.InstructionFormat;
import Core.Assembler.Instruction.InstructionSector;
import Core.Assembler.Instruction.InstructionType;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class M2RF04P2 {
    public static void main(String[] args) {
        Assembler ass= new Assembler();
        int wordSize=18;
        //TIPO 1
        ArrayList<InstructionSector> sectors1=new ArrayList<>();
        sectors1.add(new InstructionSector(0,5,"f"));
        sectors1.add(new InstructionSector(6,9,"sX"));
        sectors1.add(new InstructionSector(10,17,"K"));
        InstructionFormat format1=new  InstructionFormat(wordSize, sectors1);
        InstructionType t1=new InstructionType(format1, "TIPO 1");
        ass.addInstructionType(t1);
        System.out.println(t1.toString());
        
        //TIPO 2
        ArrayList<InstructionSector> sectors2=new ArrayList<>();
        sectors2.add(new InstructionSector(0,5,"f"));
        sectors2.add(new InstructionSector(6,9,"sX"));
        sectors2.add(new InstructionSector(10,13,"sY"));
        sectors2.add(new InstructionSector(14,17,"f2"));
        InstructionFormat format2=new  InstructionFormat(wordSize, sectors2);
        InstructionType t2=new InstructionType(format2, "TIPO 2");
        ass.addInstructionType(t2);
        System.out.println(t2.toString());
        
        //TIPO 3
        ArrayList<InstructionSector> sectors3=new ArrayList<>();
        sectors3.add(new InstructionSector(0,5,"f"));
        sectors3.add(new InstructionSector(6,9,"sX"));
        sectors3.add(new InstructionSector(10,17,"P"));
        InstructionFormat format3=new  InstructionFormat(wordSize, sectors3);
        InstructionType t3=new InstructionType(format3, "TIPO 3");
        ass.addInstructionType(t3);
        System.out.println(t3.toString());
        
        //TIPO 4
        ArrayList<InstructionSector> sectors4=new ArrayList<>();
        sectors4.add(new InstructionSector(0,17,"f"));
        InstructionFormat format4=new  InstructionFormat(wordSize, sectors4);
        InstructionType t4=new InstructionType(format4, "TIPO 4");
        ass.addInstructionType(t4);
        System.out.println(t4.toString());
        
        //TIPO 5
        ArrayList<InstructionSector> sectors5=new ArrayList<>();
        sectors5.add(new InstructionSector(0,5,"f"));
        sectors5.add(new InstructionSector(6,9,"sX"));
        sectors5.add(new InstructionSector(10,17,"f2"));
        InstructionFormat format5=new  InstructionFormat(wordSize, sectors5);
        InstructionType t5=new InstructionType(format5, "TIPO 5");
        ass.addInstructionType(t5);
        System.out.println(t5.toString());
        
        //TIPO 6
        ArrayList<InstructionSector> sectors6=new ArrayList<>();
        sectors6.add(new InstructionSector(0,5,"f"));
        sectors6.add(new InstructionSector(6,9,"sX"));
        sectors6.add(new InstructionSector(10,13,"sY"));
        sectors6.add(new InstructionSector(14,17,"f2"));
        InstructionFormat format6=new  InstructionFormat(wordSize, sectors6);
        InstructionType t6=new InstructionType(format6, "TIPO 6");
        ass.addInstructionType(t6);
        System.out.println(t6.toString());
    }
    
}
