/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ensamblatore;

import com.ensamblatore.core.architecture.Architecture;
import com.ensamblatore.core.architecture.memory.Memory;
import com.ensamblatore.core.architecture.Word;
import com.ensamblatore.core.assembler.Assembler;
import com.ensamblatore.core.assembler.Instruction.Directive;
import com.ensamblatore.core.assembler.Instruction.Instruction;
import com.ensamblatore.core.assembler.Instruction.InstructionFormat;
import com.ensamblatore.core.assembler.Instruction.InstructionSector;
import com.ensamblatore.core.assembler.Instruction.InstructionType;
import com.ensamblatore.core.assembler.Instruction.Line;
import com.ensamblatore.core.assembler.Instruction.SectionValue;
import com.ensamblatore.core.CPU;
import com.ensamblatore.core.fileManager.Serializer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author emnga
 */
public class tetsGeneral {
    
    public static void generalTestFile(){
        int wordSize=32;
        CPU cpu=new CPU("RichieCore i99");
        Memory[] memories=new Memory[1];
        memories[0]=new Memory(30,8,Architecture.DATAMEMORYNAME);
        Architecture architecture=new Architecture(memories, 1, cpu);
        architecture.setWordSize(32);
        architecture.setEndianism(Architecture.Endianess.BIG);
        architecture.setProgramInitialRegister(20);
        architecture.setDataInitialRegister(0);
        cpu.setArchitecture(architecture);
        Assembler assembler=new Assembler(cpu);        
        assembler.printSuperSintaxes=false;
        cpu.setAssembler(assembler);
        
        //TIPO R
        ArrayList<InstructionSector> sectors1=new ArrayList<>();
        sectors1.add(new InstructionSector(0,5,"OPCODE"));
        sectors1.add(new InstructionSector(6,10,"rs"));
        sectors1.add(new InstructionSector(11,15,"rt"));
        sectors1.add(new InstructionSector(16,20,"rd"));
        sectors1.add(new InstructionSector(21,25,"shamt"));
        sectors1.add(new InstructionSector(26,31,"funct"));
        InstructionFormat format1=new  InstructionFormat(wordSize, sectors1);
        InstructionType t1=new InstructionType(format1, "R");
        assembler.addInstructionType(t1);
        
        //TIPO I
        ArrayList<InstructionSector> sectors2=new ArrayList<>();
        sectors2.add(new InstructionSector(0,5,"OPCODE"));
        sectors2.add(new InstructionSector(6,10,"rs"));
        sectors2.add(new InstructionSector(11,15,"rt"));
        sectors2.add(new InstructionSector(16,31,"inmediate"));
        InstructionFormat formaB=new  InstructionFormat(wordSize, sectors2);
        InstructionType B=new InstructionType(formaB, "I");
        assembler.addInstructionType(B);
        
        //TIPO J
        ArrayList<InstructionSector> sectors3=new ArrayList<>();
        sectors3.add(new InstructionSector(0,5,"OPCODE"));
        sectors3.add(new InstructionSector(6,31,"address"));
        InstructionFormat formaC=new  InstructionFormat(wordSize, sectors3);
        InstructionType C=new InstructionType(formaC, "J");
        assembler.addInstructionType(C);
        
        //Instrucción add, ie: add d20, $ads
        Instruction add=new Instruction(t1,"ADD" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("funct", new Word(6,1,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
        });
        add.setSintax("add <VAR1|VALUE>, <VAR1|VALUE>, <VAR1>");
        add.createFormat();
        assembler.addInstruction(add);
        
        Instruction sub=new Instruction(t1,"SUB",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,1,0,0,0,1,0))
        });
        sub.setSintax("sub <VAR1>, <VAR1>, <VAR1>");
        sub.createFormat();
        assembler.addInstruction(sub);
        
        Instruction and=new Instruction(t1,"AND",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,1,0,0,1,0,0))
        });
        and.setSintax("and <VAR1>, <VAR1>, <VAR1>");
        and.createFormat();
        assembler.addInstruction(and);
        
        Instruction or=new Instruction(t1,"OR",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,1,0,0,1,0,1))
        });
        or.setSintax("or <VAR1>, <VAR1>, <VAR1>");
        or.createFormat();
        assembler.addInstruction(or);
        
        Instruction lw=new Instruction(B,"LW",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,1,0,0,0,1,1)),
        });
        lw.setSintax("lw <VAR1>, <VAR2>");
        lw.dataOrder= new String[]{"rs","inmediate","rt"};
        lw.createFormat();
        assembler.addInstruction(lw);
        
        Instruction sll=new Instruction(t1,"SLL",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("rs", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,0,0,0,0,0,0))
        });
        sll.setSintax("sll <VAR1>, <VAR1>, <VAR1>");
        sll.createFormat();
        assembler.addInstruction(sll);
        
        Instruction beq=new Instruction(B,"BEQ",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,1,0,0)),
        });
        beq.setSintax("beq <VAR1>, <VAR1>, <INTEGER>");
        beq.createFormat();
        assembler.addInstruction(beq);
        
        Instruction j=new Instruction(C,"J",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,1,0)),
        });
        j.setSintax("j <INTEGER>");
        j.createFormat();
        assembler.addInstruction(j);
        
        Line var1=new Line("VAR1");
        var1.setSintax("\\$<IDENTIFIER>");
        var1.setAsVariableUsage();
        assembler.addLine(var1);
        
        Line var2=new Line("VAR2");
        var2.setSintax("<INTEGER>\\(<VAR1>\\)");
        var2.setAsVariableUsage();
        assembler.addLine(var2);
        
        Line.init(assembler);
        Directive.init(assembler);
        String program=".data 15\n"+
                    ".var registerx = 0xa;\n" +
                    ".var registery = 0b1100;\n" +
                    "\n" +
                    "add $registerx, 4, $registery;\n"+
                    "lw $registerx, 255($registery)";
        System.out.println("Program: \n"+program);
        System.out.println("");
        Word[] result=cpu.getAssembler().runProgram(program);
        System.out.println(cpu.getAssembler().getListingFile().toString());
        System.out.println(cpu.getArchitecture().memoriesToString());
        System.out.println(Arrays.deepToString(result));
        com.ensamblatore.core.fileManager.Serializer ser=new Serializer();
        ser.writeObjectFile("real.encpu", cpu);
    }
    
    public static void riscTest(){
        int wordSize=16;
        CPU cpu=new CPU("RISC16");
        Memory[] memories=new Memory[2];
        memories[0]=new Memory(20,16,Architecture.DATAMEMORYNAME);
        memories[1]=new Memory(20,16,Architecture.PROGRAMMEMORYNAME);
        Architecture architecture=new Architecture(memories, 1, cpu);
        architecture.setWordSize(16);
        architecture.setEndianism(Architecture.Endianess.BIG);
        architecture.setProgramInitialRegister(0);
        architecture.setDataInitialRegister(0);
        cpu.setArchitecture(architecture);
        Assembler assembler=new Assembler(cpu);        
        assembler.printSuperSintaxes=false;
        cpu.setAssembler(assembler);
        
        //TIPO A
        ArrayList<InstructionSector> sectors1=new ArrayList<>();
        sectors1.add(new InstructionSector(0,4,"OPCODE"));
        sectors1.add(new InstructionSector(5,7,"RC"));
        sectors1.add(new InstructionSector(8,10,"RA"));
        sectors1.add(new InstructionSector(11,13,"RB"));
        sectors1.add(new InstructionSector(14,15,"fn"));
        InstructionFormat format1=new  InstructionFormat(wordSize, sectors1);
        InstructionType A=new InstructionType(format1, "A");
        assembler.addInstructionType(A);
        
        //TIPO B
        ArrayList<InstructionSector> sectors2=new ArrayList<>();
        sectors2.add(new InstructionSector(0,4,"OPCODE"));
        sectors2.add(new InstructionSector(5,7,"RC"));
        sectors2.add(new InstructionSector(8,15,"INM8"));
        InstructionFormat formaB=new  InstructionFormat(wordSize, sectors2);
        InstructionType B=new InstructionType(formaB, "B");
        assembler.addInstructionType(B);
        
        //TIPO C
        ArrayList<InstructionSector> sectors3=new ArrayList<>();
        sectors3.add(new InstructionSector(0,4,"OPCODE"));
        sectors3.add(new InstructionSector(5,7,"RC"));
        sectors3.add(new InstructionSector(8,10,"RA"));
        sectors3.add(new InstructionSector(14,15,"fn"));
        InstructionFormat formaC=new  InstructionFormat(wordSize, sectors3);
        InstructionType C=new InstructionType(formaC, "C");
        assembler.addInstructionType(C);
        
        //TIPO D
        ArrayList<InstructionSector> sectors4=new ArrayList<>();
        sectors4.add(new InstructionSector(0,4,"OPCODE"));
        sectors4.add(new InstructionSector(5,7,"RC"));
        sectors4.add(new InstructionSector(8,10,"RA"));
        sectors4.add(new InstructionSector(11,15,"INM5"));
        InstructionFormat formaD=new  InstructionFormat(wordSize, sectors4);
        InstructionType D=new InstructionType(formaD, "D");
        assembler.addInstructionType(D);
        
        //TIPO J
        ArrayList<InstructionSector> sectors5=new ArrayList<>();
        sectors5.add(new InstructionSector(0,4,"OPCODE"));
        sectors5.add(new InstructionSector(5,15,"INM11"));
        InstructionFormat formaJ=new  InstructionFormat(wordSize, sectors5);
        InstructionType J=new InstructionType(formaJ, "J");
        assembler.addInstructionType(J);
        
        //TIPO E
        ArrayList<InstructionSector> sectors6=new ArrayList<>();
        sectors6.add(new InstructionSector(0,4,"OPCODE"));
        sectors6.add(new InstructionSector(11,15,"INM5"));
        InstructionFormat formaE=new  InstructionFormat(wordSize, sectors6);
        InstructionType E=new InstructionType(formaE, "E");
        assembler.addInstructionType(E);
        
        
        //Instrucciones tipo A
        Instruction and=new Instruction(A,"AND" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(5,0,0,0,0,0)),
            new SectionValue("fn", new Word(2,0,0)),
        });
        and.setSintax("and <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        and.createFormat();
        assembler.addInstruction(and);
        
        Instruction or=new Instruction(A,"OR" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(5,0,0,0,0,0)),
            new SectionValue("fn", new Word(2,0,1)),
        });
        or.setSintax("or <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        or.createFormat();
        assembler.addInstruction(or);
        
        Instruction xor=new Instruction(A,"XOR" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(5,0,0,0,0,0)),
            new SectionValue("fn", new Word(2,1,0)),
        });
        xor.setSintax("xor <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        xor.createFormat();
        assembler.addInstruction(xor);
        
        Instruction nor=new Instruction(A,"NOR" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(5,0,0,0,0,0)),
            new SectionValue("fn", new Word(2,1,1)),
        });
        nor.setSintax("nor <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        nor.createFormat();
        assembler.addInstruction(nor);
        
        Instruction add=new Instruction(A,"ADD" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(5,0,0,1,0,0)),
            new SectionValue("fn", new Word(2,0,0)),
        });
        add.setSintax("add <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        add.createFormat();
        assembler.addInstruction(add);
        
        Instruction addu=new Instruction(A,"ADDU" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(5,0,0,1,0,0)),
            new SectionValue("fn", new Word(2,0,1)),
        });
        addu.setSintax("addu <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        addu.createFormat();
        assembler.addInstruction(addu);
        
        Instruction sub=new Instruction(A,"SUB" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(5,0,0,1,0,0)),
            new SectionValue("fn", new Word(2,1,0)),
        });
        sub.setSintax("sub <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        sub.createFormat();
        assembler.addInstruction(sub);
        
        Instruction subu=new Instruction(A,"SUBU" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(5,0,0,1,0,0)),
            new SectionValue("fn", new Word(2,1,1)),
        });
        subu.setSintax("subu <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        subu.createFormat();
        assembler.addInstruction(subu);
        
        Instruction seq=new Instruction(A,"SEQ",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,0,0,0)),
            new SectionValue("fn",new Word(2,1,0))
        });
        seq.setSintax("seq <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        seq.createFormat();
        assembler.addInstruction(seq);
        
        Instruction sne=new Instruction(A,"SNE",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,0,0,0)),
            new SectionValue("fn",new Word(2,1,1))
        });
        sne.setSintax("sne <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        sne.createFormat();
        assembler.addInstruction(sne);
        
        Instruction slt=new Instruction(A,"SLT",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,0,0,1)),
            new SectionValue("fn",new Word(2,0,0))
        });
        slt.setSintax("slt <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        slt.createFormat();
        assembler.addInstruction(slt);
        
        Instruction sgt=new Instruction(A,"SGT",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,0,0,1)),
            new SectionValue("fn",new Word(2,0,1))
        });
        sgt.setSintax("sgt <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        sgt.createFormat();
        assembler.addInstruction(sgt);
        
        Instruction sle=new Instruction(A,"SLE",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,0,0,1)),
            new SectionValue("fn",new Word(2,1,0))
        });
        sle.setSintax("sle <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        sle.createFormat();
        assembler.addInstruction(sle);
        
        Instruction sge=new Instruction(A,"SGE",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,0,0,1)),
            new SectionValue("fn",new Word(2,1,1))
        });
        sge.setSintax("sge <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        sge.createFormat();
        assembler.addInstruction(sge);
        
        
        //Instucciones tipo B
        Instruction andi=new Instruction(B,"ANDI",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,0,0,0,1)),
        });
        andi.setSintax("andi <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        andi.createFormat();
        assembler.addInstruction(andi);
        
        Instruction ori=new Instruction(B,"ORI",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,0,0,1,0)),
        });
        ori.setSintax("ori <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        ori.createFormat();
        assembler.addInstruction(ori);
        
        Instruction xori=new Instruction(B,"XORI",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,0,0,1,1)),
        });
        xori.setSintax("xori <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        xori.createFormat();
        assembler.addInstruction(xori);
        
        Instruction addi=new Instruction(B,"ADDI",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,0,1,0,1)),
        });
        addi.setSintax("addi <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        addi.createFormat();
        assembler.addInstruction(addi);
        
        Instruction subi=new Instruction(B,"SUBI",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,0,1,1,0)),
        });
        subi.setSintax("subi <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        subi.createFormat();
        assembler.addInstruction(subi);
        
        Instruction mvih=new Instruction(B,"MVIH",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,1,0,1)),
        });
        mvih.setSintax("mvih <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        mvih.createFormat();
        assembler.addInstruction(mvih);
        
        Instruction mvil=new Instruction(B,"MVIL",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,1,1,0)),
        });
        mvil.setSintax("mvil <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        mvil.createFormat();
        assembler.addInstruction(mvil);
        
        Instruction jr=new Instruction(B,"JR",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,1,0,0,1,0)),
        });
        jr.setSintax("jr <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        jr.createFormat();
        assembler.addInstruction(jr);
        
        Instruction jalr=new Instruction(B,"JALR",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,1,0,0,1,1)),
        });
        jalr.setSintax("jalr <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        jalr.createFormat();
        assembler.addInstruction(jalr);
        
        
        //Instrucciones tipo C
        Instruction sll=new Instruction(C,"SSL",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,0,1,1,1)),
            new SectionValue("fn",new Word(2,0,0))
        });
        sll.setSintax("sll <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        sll.createFormat();
        assembler.addInstruction(sll);
        
        Instruction srl=new Instruction(C,"SRL",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,0,1,1,1)),
            new SectionValue("fn",new Word(2,0,1))
        });
        srl.setSintax("srl <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        srl.createFormat();
        assembler.addInstruction(srl);
        
        Instruction rol=new Instruction(C,"ROL",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,0,1,1,1)),
            new SectionValue("fn",new Word(2,1,0))
        });
        rol.setSintax("rol <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        rol.createFormat();
        assembler.addInstruction(rol);
        
        Instruction ror=new Instruction(C,"ROR",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,0,1,1,1)),
            new SectionValue("fn",new Word(2,1,1))
        });
        ror.setSintax("ror <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        ror.createFormat();
        assembler.addInstruction(ror);
        
        Instruction sra=new Instruction(C,"SRA",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,0,0,0)),
            new SectionValue("fn",new Word(2,0,0))
        });
        sra.setSintax("sra <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        sra.createFormat();
        assembler.addInstruction(sra);
        
        Instruction neg=new Instruction(C,"NEG",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,0,1,1,1)),
            new SectionValue("fn",new Word(2,0,1))
        });
        neg.setSintax("neg <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>");
        neg.createFormat();
        assembler.addInstruction(neg);
       
        
        //Instrucciones tipo D
        Instruction slti=new Instruction(D,"SLTI",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,0,1,0))
        });
        slti.setSintax("slti <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        slti.createFormat();
        assembler.addInstruction(slti);
        
        Instruction lw=new Instruction(D,"LW",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,0,1,1))
        });
        lw.setSintax("lw <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        lw.createFormat();
        assembler.addInstruction(lw);
        
        Instruction sw=new Instruction(D,"SW",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,0,1,1,0,0))
        });
        sw.setSintax("sw <VAR1|DEFAULTREGISTER|VALUE>, <VAR1|DEFAULTREGISTER|VALUE>, <VALUE>");
        sw.createFormat();
        assembler.addInstruction(sw);
        
        //Instrucciones tipo E
        Instruction beq=new Instruction(E,"BEQ",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,1,0,1,0,0))
        });
        beq.setSintax("beq <VALUE>");
        beq.createFormat();
        assembler.addInstruction(beq);
        
        Instruction bne=new Instruction(E,"BNE",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,1,0,1,0,0))
        });
        bne.setSintax("bne <VALUE>");
        bne.createFormat();
        assembler.addInstruction(bne);
        
        Instruction blt=new Instruction(E,"BLT",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,1,0,1,1,0))
        });
        blt.setSintax("blt <VALUE>");
        blt.createFormat();
        assembler.addInstruction(blt);
        
        Instruction bgt=new Instruction(E,"BGT",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,1,0,1,1,1))
        });
        bgt.setSintax("bgt <VALUE>");
        bgt.createFormat();
        assembler.addInstruction(bgt);
        
        Instruction ble=new Instruction(E,"BLE",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,1,1,0,0,0))
        });
        ble.setSintax("ble <VALUE>");
        ble.createFormat();
        assembler.addInstruction(ble);
        
        Instruction bge=new Instruction(E,"BGE",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,1,1,0,0,1))
        });
        bge.setSintax("bge <VALUE>");
        bge.createFormat();
        assembler.addInstruction(bge);
        
        
        //Instrucciones tipo J
        Instruction j=new Instruction(J,"J",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,1,0,0,0,0))
        });
        j.setSintax("j <VALUE>");
        j.createFormat();
        assembler.addInstruction(j);
        
        Instruction jal=new Instruction(J,"jal",assembler,new SectionValue[]{
            new SectionValue("opcode",new Word(5,1,0,0,0,1))
        });
        jal.setSintax("jal <VALUE>");
        jal.createFormat();
        assembler.addInstruction(jal);
        
        Line var1=new Line("VAR1");
        var1.setSintax("\\$<IDENTIFIER>");
        var1.setAsVariableUsage();
        assembler.addLine(var1);
        
        Line var2=new Line("VAR2");
        var2.setSintax("<INTEGER>\\(<VAR1|DEFAULTREGISTER>\\)");
        var2.setAsVariableUsage();
        assembler.addLine(var2);
        Line.init(assembler);
        Directive.init(assembler);
        String program="mvih R1, 0x81\n" +
            "mvil R1, 0x95\n" +
            "mvil R2, 0x80\n" +
            "add R4, R3, R1\n" +
            "addi R3, 2\n" +
            "ori R3, 0xD4\n" +
            "and R3, R3, R2\n" +
            "neg R5, R4\n" +
            "addi R5, 1\n" +
            "mvih R5, 0x80\n" +
            "sra R5, R5\n" +
            "slt R6, R5, R4\n" +
            "seq R2, R2, R3\n" +
            "beq 2\n" +
            "and R2, R2, R6\n" +
            "subu R4, R4, R6\n" +
            "sw R1, R6, 1\n" +
            "lw R3, R6, 1\n";
        System.out.println("Program: \n"+program);
        System.out.println("");
        Word[] result=cpu.getAssembler().runProgram(program);
        System.out.println(cpu.getAssembler().getListingFile().toString());
        System.out.println(cpu.getArchitecture().memoriesToString());
        System.out.println(Arrays.deepToString(result));
        com.ensamblatore.core.fileManager.Serializer ser=new Serializer();
        ser.writeObjectFile("risc16.encpu", cpu);
    }
    
    public static void main(String[] args) {
        generalTestFile();
        riscTest();
        
    }
}
