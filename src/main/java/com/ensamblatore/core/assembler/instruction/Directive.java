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
package com.ensamblatore.core.assembler.instruction;

import com.ensamblatore.core.architecture.Architecture;
import com.ensamblatore.core.architecture.memory.Memory;
import com.ensamblatore.core.architecture.Word;
import com.ensamblatore.core.assembler.Assembler;
import com.ensamblatore.exceptions.ParsingException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author emnga
 */
public abstract class Directive extends Line implements Serializable {

    public abstract void execute(String line);
    public static ArrayList<Directive> DEFAULT_DIRECTIVES;
    private final Assembler ass;
    private int priority;

    private Directive(String identifier, String toSintax, Assembler ass, int priority) {
        super(identifier, toSintax);
        this.ass = ass;
        this.id = Line.ID_DIRECTIVE;
        this.priority = priority;
    }

    public Assembler getAss() {
        return ass;
    }

    public int getPriority() {
        return this.priority;
    }
    
    public void setPriority(int priority){
        this.priority=priority;
    }

    public int priorityIncreas(int increase) {
        this.priority += increase;
        return this.priority;
    }

    public int priorityIncreaseGreaterTo(Directive based, int increase) {
        if (based.priority < this.priority) {
            return this.priority;
        }
        this.priority = based.getPriority() + increase;
        return this.priority;

    }

    public static void init(Assembler ass) {
        ArrayList<Directive> ret = new ArrayList<>();
        ret.add(new Directive("WORDALLOCATION", ".w <IDENTIFIER> <VALUE>", ass, 1) {
            @Override
            public void execute(String line) {
                int memoriesCount = ass.getParent().architecture.getMemoriesCount();
                ArrayList<String> arguments = ass.getArguments(line, this);
                String identifierName = "variable";
                String value = "0";
                for (String argument : arguments) {
                    Value val = new Value(argument);
                    switch (val.getType()) {
                        case 1:
                            identifierName = val.getRaw();
                            break;
                        case -1:
                            throw new ParsingException("Error in argument sintax \"" + val.getRaw() + "\"");
                        default:
                            value = val.toBinary().getRawValue();
                            break;
                    }
                }
                if (memoriesCount > 1) {
                    Memory data = ass.getParent().getArchitecture().getMemoryFromIdentifier(Architecture.DATAMEMORYNAME);
                    int writePointer = this.getAss().memoriesWritePointers[data.id];
                    Word parsed = Word.parseWord(value);
                    int idid = data.setWord(writePointer, parsed);;
                    this.getAss().getListingFile().add(identifierName, parsed, writePointer, idid);
                    this.getAss().memoriesWritePointers[data.id] += idid;
                } else {
                    Memory program = ass.getParent().getArchitecture().getMemory()[0];
                    int writePointer = this.getAss().memoriesWritePointers[0];
                    Word parsed = Word.parseWord(value);
                    int idid = program.setWord(writePointer, parsed);
                    this.getAss().getListingFile().add(identifierName, parsed, writePointer, idid);
                    this.getAss().memoriesWritePointers[program.id] += idid;
                }
            }
        });
        ret.add(new Directive("VARIABLEDECLARATION", ".var <IDENTIFIER> = <VALUE>", ass, 1) {
            @Override
            public void execute(String line) {
                int memoriesCount = ass.getParent().architecture.getMemoriesCount();
                ArrayList<String> arguments = ass.getArguments(line, this);
                String identifierName = "";
                String value = "0";
                for (String argument : arguments) {
                    Value val = new Value(argument);
                    switch (val.getType()) {
                        case 1:
                            identifierName = val.getRaw();
                            break;
                        case -1:
                            throw new ParsingException("Error in argument sintax \"" + val.getRaw() + "\"");
                        default:
                            value = val.toBinary().getRawValue();
                            break;
                    }
                }
                if (memoriesCount > 1) {
                    Memory data = ass.getParent().getArchitecture().getMemoryFromIdentifier(Architecture.DATAMEMORYNAME);
                    int writePointer = this.getAss().memoriesWritePointers[data.id];
                    Word parsed = Word.parseWord(value);
                    int idid = data.setWord(writePointer, parsed);
                    this.getAss().getListingFile().add(identifierName, parsed, writePointer, idid);
                    this.getAss().memoriesWritePointers[data.id] += idid;
                } else {
                    Memory data = ass.getParent().getArchitecture().getMemory()[0];
                    int writePointer = this.getAss().memoriesWritePointers[1];
                    Word parsed = Word.parseWord(value);
                    int idid = data.setWord(writePointer, parsed);
                    this.getAss().getListingFile().add(identifierName, parsed, writePointer, idid);
                    this.getAss().memoriesWritePointers[1] += idid;
                }
            }
        });
        ret.add(new Directive("ENDIANESSCHANGE", ".endianess <IDENTIFIER>", ass, 2) {
            @Override
            public void execute(String line) {
                ArrayList<String> arguments = ass.getArguments(line, this);
                String ident = "";
                for (String argument : arguments) {
                    Value val = new Value(argument);
                    switch (val.getType()) {
                        case 1:
                            ident = val.getRaw();
                            break;
                        case -1:
                            throw new ParsingException("Error in argument sintax \"" + val.getRaw() + "\" in " + line);
                        default:
                            throw new ParsingException("Invalid argument \"" + val.getRaw() + "\" in " + line);
                    }
                }
                if (ident.equals(Architecture.BIGENDIANNAME)) {
                    ass.getParent().architecture.setEndianism(Architecture.Endianess.BIG);
                } else if (ident.equals(Architecture.LITTLEENDIANNAME)) {
                    ass.getParent().architecture.setEndianism(Architecture.Endianess.LITTLE);
                } else {
                    throw new ParsingException("Invalid argument \"" + ident + "\" in " + line);
                }
            }
        });
        ret.add(new Directive("PROGRAMINITIALREGISTER", ".program <VALUE>", ass, 3) {
            @Override
            public void execute(String line) {
                ArrayList<String> arguments = ass.getArguments(line, this);
                String value = "";
                for (String argument : arguments) {
                    Value val = new Value(argument);
                    switch (val.getType()) {
                        case 0:
                            value = val.toInteger().getRawValue();
                            break;
                        case -1:
                            throw new ParsingException("Error in argument sintax \"" + val.getRaw() + "\" in " + line);
                        default:
                            throw new ParsingException("Invalid argument \"" + val.getRaw() + "\" in " + line);
                    }
                }
                ass.getParent().getArchitecture().setProgramInitialRegister(Integer.parseInt(value));
                ass.memoriesWritePointers = ass.getParent().getArchitecture().initMemoryCounters();
            }
        });
        ret.add(new Directive("DATAINITIALREGISTER", ".data <VALUE>", ass, 3) {
            @Override
            public void execute(String line) {
                ArrayList<String> arguments = ass.getArguments(line, this);
                String value = "";
                for (String argument : arguments) {
                    Value val = new Value(argument);
                    switch (val.getType()) {
                        case -1:
                            throw new ParsingException("Error in argument sintax \"" + val.getRaw() + "\" in " + line);
                        case 1:
                            throw new ParsingException("Invalid argument \"" + val.getRaw() + "\" in " + line);
                        default:
                            value = val.toInteger().getRawValue();
                            break;
                    }
                }
                ass.getParent().getArchitecture().setDataInitialRegister(Integer.parseInt(value));
                ass.memoriesWritePointers = ass.getParent().getArchitecture().initMemoryCounters();

            }
        });
        Directive.DEFAULT_DIRECTIVES = ret;
        if (ass != null) {
            for (int i = 0; i < Directive.DEFAULT_DIRECTIVES.size(); i++) {
                
                ass.addDefaultDirective(Directive.DEFAULT_DIRECTIVES.get(i));
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString()).append(';');
        sb.append("Assembler: ").append(this.ass == null).append(';');
        sb.append("Priority: ").append(this.priority);
        return sb.toString();
    }
}
