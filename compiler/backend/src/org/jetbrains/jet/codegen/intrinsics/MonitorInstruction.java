/*
 * Copyright 2010-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.codegen.intrinsics;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.codegen.ExpressionCodegen;
import org.jetbrains.jet.codegen.StackValue;
import org.jetbrains.jet.lang.psi.JetElement;
import org.jetbrains.jet.lang.psi.JetExpression;
import org.jetbrains.org.objectweb.asm.Opcodes;
import org.jetbrains.org.objectweb.asm.Type;
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter;

import java.util.Arrays;
import java.util.List;

import static org.jetbrains.jet.lang.resolve.bindingContextUtil.BindingContextUtilPackage.getResolvedCallWithAssert;
import static org.jetbrains.jet.lang.resolve.java.AsmTypeConstants.OBJECT_TYPE;

public class MonitorInstruction extends IntrinsicMethod {

    public static final MonitorInstruction MONITOR_ENTER = new MonitorInstruction(Opcodes.MONITORENTER);
    public static final MonitorInstruction MONITOR_EXIT = new MonitorInstruction(Opcodes.MONITOREXIT);

    private final int opcode;

    private MonitorInstruction(int opcode) {
        this.opcode = opcode;
    }

    @NotNull
    @Override
    protected Type generateImpl(
            @NotNull ExpressionCodegen codegen,
            @NotNull InstructionAdapter v,
            @NotNull Type returnType,
            @Nullable PsiElement element,
            @Nullable List<JetExpression> arguments,
            @Nullable StackValue receiver
    ) {
        assert element != null : "Element should not be null";

        codegen.pushMethodArgumentsWithoutCallReceiver(
                getResolvedCallWithAssert((JetElement) element, codegen.getBindingContext()),
                Arrays.asList(OBJECT_TYPE),
                false,
                codegen.defaultCallGenerator
        );

        v.visitInsn(opcode);
        return Type.VOID_TYPE;
    }
}
