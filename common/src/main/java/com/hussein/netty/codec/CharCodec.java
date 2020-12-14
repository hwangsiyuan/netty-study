package com.hussein.netty.codec;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * <p>Title: CharCodec</p>
 * <p>Description: </p>
 * <p>Company: www.hussein.com</p>
 *
 * @author hwangsy
 * @date 2019/6/29 2:33 PM
 */
public class CharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder,CharToByteEncoder> {

    public CharCodec() {
        super(new ByteToCharDecoder(),new CharToByteEncoder());
    }
}
