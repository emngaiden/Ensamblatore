/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Core.Assembler.AddressingMode;
import Core.Assembler.Assembler;
import Core.Assembler.Instruction.InstructionFormat;
import Core.Assembler.Instruction.InstructionSector;
import Core.Assembler.Instruction.InstructionType;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class M2RF03P1_2 {
    public static void main(String[] args) {
        Assembler ass1=new Assembler();
        /*
        *Tipo R: Identifier= R
        *Tipo I: Identifier= I
        *Tipo J: Identifier= J
        */
        //TIPO R
        InstructionType t1=new InstructionType("R");
        ass1.addInstructionType(t1);
        System.out.println(t1.toString());
        
        //TIPO I
        InstructionType t2=new InstructionType("I");
        ass1.addInstructionType(t2);
        System.out.println(t2.toString());
        
        //TIPO J
        InstructionType t3=new InstructionType("J");
        ass1.addInstructionType(t3);
        System.out.println(t3.toString());
        
        Assembler ass2=new Assembler();
        /*
        *Tipo 1: Identifier= Format 1
        *Tipo 2: Identifier= Format 2
        *Tipo 3: Identifier= Format 3
        */
        //TIPO 1
        InstructionType f1=new InstructionType("Format 1");
        ass2.addInstructionType(f1);
        System.out.println(f1.toString());
        
        //TIPO 2
        InstructionType f2=new InstructionType("Format 2");
        ass2.addInstructionType(f2);
        System.out.println(f2.toString());
        
        //TIPO 3
        InstructionType f3=new InstructionType("Format 3");
        ass2.addInstructionType(f3);
        System.out.println(f3.toString());
        
    }
}
