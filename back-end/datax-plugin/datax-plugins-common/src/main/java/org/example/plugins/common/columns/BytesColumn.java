package org.example.plugins.common.columns;

import org.apache.commons.lang3.ArrayUtils;
import org.example.plugins.common.exceptions.ColumnError;
import org.example.plugins.common.exceptions.SystemException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class BytesColumn extends Column {

    public BytesColumn() {
        this(null);
    }

    public BytesColumn(byte[] bytes) {
        super(Type.BYTES, null == bytes ? 0 : bytes.length, ArrayUtils.clone(bytes));
    }

    @Override
    public Long asLong() {
        throw SystemException.newException(
                ColumnError.CONVERT_NOT_SUPPORT, "Bytes类型不能转为Long .");
    }

    @Override
    public Double asDouble() {
        throw SystemException.newException(
                ColumnError.CONVERT_NOT_SUPPORT, "Bytes类型不能转为Double .");
    }

    @Override
    public String asString() {
        if (null == this.getData()) {
            return null;
        }

        try {
            return ColumnCast.bytes2String(this);
        } catch (Exception e) {
            throw SystemException.newException(
                    ColumnError.CONVERT_NOT_SUPPORT,
                    String.format("Bytes[%s]不能转为String .", this.toString()));
        }
    }

    @Override
    public Date asDate() {
        throw SystemException.newException(
                ColumnError.CONVERT_NOT_SUPPORT, "Bytes类型不能转为Date .");
    }

    @Override
    public byte[] asBytes() {
        if (null == this.getData()) {
            return null;
        }

        return (byte[]) this.getData();
    }

    @Override
    public Boolean asBoolean() {
        throw SystemException.newException(
                ColumnError.CONVERT_NOT_SUPPORT, "Bytes类型不能转为Boolean .");
    }

    @Override
    public BigDecimal asBigDecimal() {
        throw SystemException.newException(
                ColumnError.CONVERT_NOT_SUPPORT, "Bytes类型不能转为BigDecimal .");
    }

    @Override
    public BigInteger asBigInteger() {
        throw SystemException.newException(
                ColumnError.CONVERT_NOT_SUPPORT, "Bytes类型不能转为BigInteger .");
    }
}
