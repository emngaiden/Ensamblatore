/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ensamblatore;

import com.ensamblatore.core.assembler.Assembler;
import com.ensamblatore.core.assembler.Instruction.InstructionFormat;
import com.ensamblatore.core.assembler.Instruction.InstructionSector;
import com.ensamblatore.core.assembler.Instruction.InstructionType;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class M2RF04P1 {
    public static void main(String[] args) {
        Assembler ass1=new Assembler();
        int wordSize1=32;
        /*
        *Tipo R: Identifier= R
        *Tipo I: Identifier= I
        *Tipo J: Identifier= J
        */
        //TIPO R
        ArrayList<InstructionSector> sectors1=new ArrayList<>();
        sectors1.add(new InstructionSector(0,5,"OPCODE"));
        sectors1.add(new InstructionSector(6,10,"rs"));
        sectors1.add(new InstructionSector(11,15,"rt"));
        sectors1.add(new InstructionSector(16,20,"rd"));
        sectors1.add(new InstructionSector(21,25,"shamt"));
        sectors1.add(new InstructionSector(26,31,"funct"));
        InstructionFormat format1=new  InstructionFormat(wordSize1, sectors1);
        InstructionType t1=new InstructionType(format1, "R");
        ass1.addInstructionType(t1);
        System.out.println(t1.toString());
        
        //TIPO I
        ArrayList<InstructionSector> sectors2=new ArrayList<>();
        sectors2.add(new InstructionSector(0,5,"OPCODE"));
        sectors2.add(new InstructionSector(6,10,"rs"));
        sectors2.add(new InstructionSector(11,15,"rt"));
        sectors2.add(new InstructionSector(16,31,"inmediate"));
        InstructionFormat format2=new  InstructionFormat(wordSize1, sectors2);
        InstructionType t2=new InstructionType(format2, "I");
        ass1.addInstructionType(t2);
        System.out.println(t2.toString());
        
        //TIPO J
        ArrayList<InstructionSector> sectors3=new ArrayList<>();
        sectors3.add(new InstructionSector(0,5,"OPCODE"));
        sectors3.add(new InstructionSector(6,31,"address"));
        InstructionFormat format3=new  InstructionFormat(wordSize1, sectors3);
        InstructionType t3=new InstructionType(format3, "J");
        ass1.addInstructionType(t3);
        System.out.println(t3.toString());
        
    }
}
