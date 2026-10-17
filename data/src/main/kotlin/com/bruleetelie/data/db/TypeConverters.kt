package com.bruleetelie.data.db

import androidx.room.TypeConverter
import java.math.BigDecimal

class BruleeConverters {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }
}
