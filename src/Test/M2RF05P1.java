/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Core.Assembler.AddressingMode;
import Core.Assembler.Assembler;
import Core.Assembler.Format.Sintax;
import Core.Assembler.Instruction.Instruction;
import Core.Assembler.Instruction.InstructionFormat;
import Core.Assembler.Instruction.InstructionSector;
import Core.Assembler.Instruction.InstructionType;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class M2RF05P1 {
    public static void main(String[] args) {
        /*
        Instrucción ADD sX, kk. Ambas direcciones utilizan modo de direccionamiento directo.
        Instrucción INPUT sX, (sY). La dirección sY utiliza modo de direccionamiento indirecto
        */
        int wordSize=18;
        ArrayList<InstructionSector> sectors6=new ArrayList<>();
        sectors6.add(new InstructionSector(0,5,"f"));
        sectors6.add(new InstructionSector(6,11,"sX",AddressingMode.INMEDIATO));
        sectors6.add(new InstructionSector(12,17,"sY",AddressingMode.INMEDIATO));
        InstructionFormat format6=new  InstructionFormat(wordSize, sectors6);
        InstructionType t6=new InstructionType(format6, "TIPO 6");
        Instruction add=new Instruction(t6);
        add.setSintax("add <var1>, <var1>");
        System.out.println(t6.toString());
        
         ArrayList<InstructionSector> sectors7=new ArrayList<>();
        sectors7.add(new InstructionSector(0,5,"f"));
        sectors7.add(new InstructionSector(6,11,"sX",AddressingMode.INMEDIATO));
        sectors7.add(new InstructionSector(12,17,"sY",AddressingMode.REGISTROINDIRECTO));
        InstructionFormat format7=new  InstructionFormat(wordSize, sectors7);
        InstructionType t7=new InstructionType(format7, "TIPO 7");
        Instruction input=new Instruction(t7);
        add.setSintax("input <var1>, <var2>");
        System.out.println(t6.toString());
        
        
        
    }
}
