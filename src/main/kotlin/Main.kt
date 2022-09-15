@Suppress("MemberVisibilityCanBePrivate")
class BytesReader(private val data: IntArray) {

  var position = 0

  private val tmpBuffer = IntArray(4) { 0 }

  fun read1Byte(customPosition: Int = position): Int {
    seek(customPosition, 1)
    if (updateTmpBuffer(1)) return (tmpBuffer[0])
    return -1
  }

  fun read2Bytes(customPosition: Int = position): Int {
    seek(customPosition, 2)
    if (updateTmpBuffer(2)) {
      return tmpBuffer[0] shl 8 or
              tmpBuffer[1]
    }
    return -1
  }

  fun read3Bytes(customPosition: Int = position): Int {
    seek(customPosition, 3)
    if (updateTmpBuffer(3)) {
      return tmpBuffer[0] shl 16 or
              (tmpBuffer[1] shl 8 or
                      tmpBuffer[2])
    }
    return -1
  }

  fun read4Bytes(customPosition: Int = position): Int {
    seek(customPosition, 4)
    if (updateTmpBuffer(4)) {
      return tmpBuffer[0] shl 24 or
              (tmpBuffer[1] shl 16 or
                      (tmpBuffer[2] shl 8 or
                              tmpBuffer[3]))
    }
    return -1
  }

  fun seek(pos: Int, nBytes: Int): Int {
    if (data.size <= pos + nBytes) {
      return -1
    }

    position = pos
    return position
  }

  private fun updateTmpBuffer(nBytes: Int): Boolean {
    if (position + nBytes >= data.size) return false
    repeat(nBytes) { tmpBuffer[it] = data[position + it] }
    position += nBytes
    return true
  }
}

@Suppress("MemberVisibilityCanBePrivate")
class BytesWriter(private val nBytes: Int) {

  var position = 0
  val data = IntArray(nBytes) { 0 }

  fun writeByte(byte: Int, customPosition: Int = position) {
    if (seek(customPosition) == -1) return
    if (position + 1 >= nBytes) return
    data[position++] = byte
  }

  fun writeInt16(int16: Int, customPosition: Int = position) {
    writeByte((int16 shr 8) and 0xFF, customPosition + 0)
    writeByte((int16 shr 0) and 0xFF, customPosition + 1)
  }

  fun writeInt24(int24: Int, customPosition: Int = position) {
    writeByte((int24 shr 16) and 0xFF, customPosition + 0)
    writeByte((int24 shr 8) and 0xFF, customPosition + 1)
    writeByte((int24 shr 0) and 0xFF, customPosition + 2)
  }

  fun seek(pos: Int): Int {
    if (pos >= nBytes) {
      return -1
    }
    position = pos
    return position
  }
}

fun main() {
  val writer = BytesWriter(4)
  writer.writeInt24(0xaabbcc)
  println(writer.data.map { Integer.toHexString(it) })
}
