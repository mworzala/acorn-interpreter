package org.acornlang

import java.lang.foreign.Linker
import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.SegmentAllocator
import java.lang.foreign.ValueLayout

fun main() {

    val puts = Linker.nativeLinker().downcallHandle(
        Linker.nativeLinker().defaultLookup()
            .lookup("puts")
            .orElseThrow(),
        FunctionDescriptor.of(
            ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS,
        ),
    )

    val allocator = SegmentAllocator.implicitAllocator()
    val str = allocator.allocateUtf8String("Hello, native!")

    puts.invoke(str)

}