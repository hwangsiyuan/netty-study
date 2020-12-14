package com.hussein.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * <p>Title: ByteBufTest</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/6/28 10:42 AM
 */
public class ByteBufTest {

    public static void main(String[] args) {
        //测试slice方法
        testSlice();
        //测试copy方法
        testCopy();
        //测试get/set 方法
        testGetSet();
        //test read/write方法
        testReadWrite();
    }

    private static void testSlice() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", CharsetUtil.UTF_8);
        ByteBuf slice = byteBuf.slice(0, 14);
        slice.setByte(0, (byte) 'J');
        System.out.println(byteBuf.getByte(0) == slice.getByte(0));
    }

    private static void testCopy() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", CharsetUtil.UTF_8);
        ByteBuf copy = byteBuf.copy(0, 14);
        copy.setByte(0, (byte) 'J');
        System.out.println(byteBuf.getByte(0) == copy.getByte(0));
    }

    private static void testGetSet() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", CharsetUtil.UTF_8);
        System.out.println((char) byteBuf.getByte(0));
        int readerIndex = byteBuf.readerIndex();
        int writerIndex = byteBuf.writerIndex();
        byteBuf.setByte(0, (byte) 'B');
        System.out.println(readerIndex==byteBuf.readerIndex());
        System.out.println(writerIndex==byteBuf.writerIndex());
    }

    private static void testReadWrite() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", CharsetUtil.UTF_8);
        System.out.println((char) byteBuf.readByte());
        int readerIndex = byteBuf.readerIndex();
        int writerIndex = byteBuf.writerIndex();
        byteBuf.writeByte((byte) 'B');
        System.out.println(readerIndex==byteBuf.readerIndex());
        System.out.println(writerIndex==byteBuf.writerIndex());
    }
}
