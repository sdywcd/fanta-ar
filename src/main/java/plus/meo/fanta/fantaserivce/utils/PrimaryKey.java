package plus.meo.fanta.fantaserivce.utils;

import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 采用mongoDB主键生成策略生成mysql等关系型数据库主键 利用空间换时间的想法，存储24个16进制字符为主键 前四位为16进制时间戳
 * 接下来3个字节为16进制主机唯一标识符 然后的4个字节为16进制进程号
 * 最后为自增计数器，理论上同一秒内最高可产生256的3次方等于16777216条记录的唯一性
 */
public class PrimaryKey implements Comparable<PrimaryKey>, java.io.Serializable {

	private static final long serialVersionUID = -402805027221692295L;
	static final Logger LOGGER = Logger.getLogger("PrimaryKey");

	/**
	 * 得到一个主键对象
	 *
	 * @return the new id
	 */
	public static PrimaryKey get() {
		return new PrimaryKey();
	}

	/**
	 * <p>
	 * Creates an PrimaryKey using time, machine and inc values. The Java driver
	 * used to create all PrimaryKeys this way, but it does not match the <a
	 * href="http://docs.mongodb.org/manual/reference/object-id/">PrimaryKey
	 * specification</a>, which requires four values, not three. This major
	 * release of the Java driver conforms to the specification, but still
	 * supports clients that are relying on the behavior of the previous major
	 * release by providing this explicit factory method that takes three
	 * parameters instead of four.
	 * </p>
	 *
	 * <p>
	 * Ordinary users of the driver will not need this method. It's only for
	 * those that have written there own BSON decoders.
	 * </p>
	 *
	 * <p>
	 * NOTE: This will not break any application that use PrimaryKeys. The
	 * 12-byte representation will be round-trippable from old to new driver
	 * releases.
	 * </p>
	 *
	 * @param time
	 *            time in seconds
	 * @param machine
	 *            machine ID
	 * @param inc
	 *            incremental value
	 * @return a new {@code PrimaryKey} created from the given values
	 * @since 2.12.0
	 */
	public static PrimaryKey createFromLegacyFormat(final int time, final int machine, final int inc) {
		return new PrimaryKey(time, machine, inc);
	}

	/**
	 * 检查是否为一个主键对象
	 *
	 * @param s
	 *            a potential PrimaryKey as a String.
	 * @return whether the string could be an object id
	 * @throws IllegalArgumentException
	 *             if hexString is null
	 */
	public static boolean isValid(String s) {
		if (s == null)
			return false;

		final int len = s.length();
		if (len != 24)
			return false;

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c >= '0' && c <= '9')
				continue;
			if (c >= 'a' && c <= 'f')
				continue;
			if (c >= 'A' && c <= 'F')
				continue;

			return false;
		}

		return true;
	}

	/**
	 * Turn an object into an {@code PrimaryKey}, if possible. Strings will be
	 * converted into {@code PrimaryKey}s, if possible, and {@code PrimaryKey}s
	 * will be cast and returned. Passing in {@code null} returns {@code null}.
	 *
	 * @param o
	 *            the object to convert
	 * @return an {@code PrimaryKey} if it can be massaged, null otherwise
	 * @deprecated This method is NOT a part of public API and will be dropped
	 *             in 3.x versions.
	 */
	@Deprecated
	public static PrimaryKey massageToPrimaryKey(Object o) {
		if (o == null)
			return null;

		if (o instanceof PrimaryKey)
			return (PrimaryKey) o;

		if (o instanceof String) {
			String s = o.toString();
			if (isValid(s))
				return new PrimaryKey(s);
		}

		return null;
	}

	/**
	 * Constructs a new instance using the given date.
	 *
	 * @param time
	 *            the date
	 */
	public PrimaryKey(Date time) {
		this(time, _genmachine, _nextInc.getAndIncrement());
	}

	/**
	 * Constructs a new instances using the given date and counter.
	 *
	 * @param time
	 *            the date
	 * @param inc
	 *            the counter
	 * @throws IllegalArgumentException
	 *             if the high order byte of counter is not zero
	 */
	public PrimaryKey(Date time, int inc) {
		this(time, _genmachine, inc);
	}

	/**
	 * Constructs an PrimaryKey using time, machine and inc values. The Java
	 * driver has done it this way for a long time, but it does not match the <a
	 * href="http://docs.mongodb.org/manual/reference/object-id/">PrimaryKey
	 * specification</a>, which requires four values, not three. The next major
	 * release of the Java driver will conform to this specification, but will
	 * still need to support clients that are relying on the current behavior.
	 * To that end, this constructor is now deprecated in favor of the more
	 * explicit factory method PrimaryKey#createFromLegacyFormat(int, int,
	 * int)}, and in the next major release this constructor will be removed.
	 *
	 * @param time
	 *            the date
	 * @param machine
	 *            the machine identifier
	 * @param inc
	 *            the counter
	 * @see PrimaryKey#createFromLegacyFormat(int, int, int)
	 * @deprecated {@code PrimaryKey}'s constructed this way do not conform to
	 *             the <a
	 *             href="http://docs.mongodb.org/manual/reference/object-id/"
	 *             >PrimaryKey specification</a>. Please use
	 *             {@link org.bson.types.PrimaryKey#PrimaryKey(byte[])} or
	 *             {@link PrimaryKey#createFromLegacyFormat(int, int, int)}
	 *             instead.
	 */
	@Deprecated
	public PrimaryKey(Date time, int machine, int inc) {
		_time = (int) (time.getTime() / 1000);
		_machine = machine;
		_inc = inc;
		_new = false;
	}

	/**
	 * Creates a new instance from a string.
	 * 
	 * @param s
	 *            the string to convert
	 * @throws IllegalArgumentException
	 *             if the string is not a valid id
	 */
	public PrimaryKey(String s) {
		this(s, false);
	}

	/**
	 * Constructs a new instance of {@code PrimaryKey} from a string.
	 * 
	 * @param s
	 *            the string representation of PrimaryKey. Can contains only
	 *            [0-9]|[a-f]|[A-F] characters.
	 * @param babble
	 *            if {@code true} - convert to 'babble' PrimaryKey format
	 *
	 * @deprecated 'babble' format is deprecated. Please use
	 *             {@link #PrimaryKey(String)} instead.
	 */
	@Deprecated
	public PrimaryKey(String s, boolean babble) {

		if (!isValid(s))
			throw new IllegalArgumentException("invalid PrimaryKey [" + s + "]");

		if (babble)
			s = babbleToMongod(s);

		byte b[] = new byte[12];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
		}
		ByteBuffer bb = ByteBuffer.wrap(b);
		_time = bb.getInt();
		_machine = bb.getInt();
		_inc = bb.getInt();
		_new = false;
	}

	/**
	 * Constructs an PrimaryKey given its 12-byte binary representation.
	 * 
	 * @param b
	 *            a byte array of length 12
	 */
	public PrimaryKey(byte[] b) {
		if (b.length != 12)
			throw new IllegalArgumentException("need 12 bytes");
		ByteBuffer bb = ByteBuffer.wrap(b);
		_time = bb.getInt();
		_machine = bb.getInt();
		_inc = bb.getInt();
		_new = false;
	}

	/**
	 * Constructs an PrimaryKey using time, machine and inc values. The Java
	 * driver has done it this way for a long time, but it does not match the <a
	 * href="http://docs.mongodb.org/manual/reference/object-id/">PrimaryKey
	 * specification</a>, which requires four values, not three. The next major
	 * release of the Java driver will conform to this specification, but we
	 * will still need to support clients that are relying on the current
	 * behavior. To that end, this constructor is now deprecated in favor of the
	 * more explicit factory method PrimaryKey#createFromLegacyFormat(int, int,
	 * int)}, and in the next major release this constructor will be removed.
	 *
	 * @param time
	 *            time in seconds
	 * @param machine
	 *            machine ID
	 * @param inc
	 *            incremental value
	 * @see PrimaryKey#createFromLegacyFormat(int, int, int)
	 * @deprecated {@code PrimaryKey}'s constructed this way do not conform to
	 *             the <a
	 *             href="http://docs.mongodb.org/manual/reference/object-id/"
	 *             >PrimaryKey specification</a>. Please use
	 *             {@link org.bson.types.PrimaryKey#PrimaryKey(byte[])} or
	 *             {@link PrimaryKey#createFromLegacyFormat(int, int, int)}
	 *             instead.
	 */
	@Deprecated
	public PrimaryKey(int time, int machine, int inc) {
		_time = time;
		_machine = machine;
		_inc = inc;
		_new = false;
	}

	/**
	 * 创建一个主键对象
	 */
	public PrimaryKey() {
		_time = (int) (System.currentTimeMillis() / 1000);// 时间戳
		_machine = _genmachine;// 机器码和进程号
		_inc = _nextInc.getAndIncrement();// 自增
		_new = true;// 是否为创建新主键
	}

	@Override
	public int hashCode() {
		int x = _time;
		x += (_machine * 111);
		x += (_inc * 17);
		return x;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		PrimaryKey other = massageToPrimaryKey(o);
		if (other == null)
			return false;

		return _time == other._time && _machine == other._machine && _inc == other._inc;
	}

	/**
	 * @deprecated 'babble' format is deprecated and will be removed in 3.0.
	 *             Please use {@link #toHexString()} instead.
	 */
	@Deprecated
	public String toStringBabble() {
		return babbleToMongod(toStringMongod());
	}

	/**
	 * Converts this instance into a 24-byte hexadecimal string representation.
	 *
	 * @return a string representation of the PrimaryKey in hexadecimal format
	 */
	public String toHexString() {
		final StringBuilder buf = new StringBuilder(24);

		for (final byte b : toByteArray()) {
			buf.append(String.format("%02x", b & 0xff));
		}

		return buf.toString();
	}

	/**
	 * @return a string representation of the PrimaryKey in hexadecimal format
	 * @see PrimaryKey#toHexString()
	 * @deprecated use {@link #toHexString()}
	 */
	@Deprecated
	public String toStringMongod() {
		byte b[] = toByteArray();

		StringBuilder buf = new StringBuilder(24);

		for (int i = 0; i < b.length; i++) {
			int x = b[i] & 0xFF;
			String s = Integer.toHexString(x);
			if (s.length() == 1)
				buf.append("0");
			buf.append(s);
		}

		return buf.toString();
	}

	/**
	 * Convert to a byte array. Note that the numbers are stored in big-endian
	 * order.
	 *
	 * @return the byte array
	 */
	public byte[] toByteArray() {
		byte b[] = new byte[12];
		ByteBuffer bb = ByteBuffer.wrap(b);
		// by default BB is big endian like we need
		bb.putInt(_time);
		bb.putInt(_machine);
		bb.putInt(_inc);
		return b;
	}

	static String _pos(String s, int p) {
		return s.substring(p * 2, (p * 2) + 2);
	}

	/**
	 * @deprecated This method is NOT a part of public API and will be dropped
	 *             in 3.x versions.
	 */
	@Deprecated
	public static String babbleToMongod(String b) {
		if (!isValid(b))
			throw new IllegalArgumentException("invalid object id: " + b);

		StringBuilder buf = new StringBuilder(24);
		for (int i = 7; i >= 0; i--)
			buf.append(_pos(b, i));
		for (int i = 11; i >= 8; i--)
			buf.append(_pos(b, i));

		return buf.toString();
	}

	public String toString() {
		return toStringMongod();
	}

	int _compareUnsigned(int i, int j) {
		long li = 0xFFFFFFFFL;
		li = i & li;
		long lj = 0xFFFFFFFFL;
		lj = j & lj;
		long diff = li - lj;
		if (diff < Integer.MIN_VALUE)
			return Integer.MIN_VALUE;
		if (diff > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		return (int) diff;
	}

	public int compareTo(PrimaryKey id) {
		if (id == null)
			return -1;

		int x = _compareUnsigned(_time, id._time);
		if (x != 0)
			return x;

		x = _compareUnsigned(_machine, id._machine);
		if (x != 0)
			return x;

		return _compareUnsigned(_inc, id._inc);
	}

	/**
	 * Gets the timestamp (number of seconds since the Unix epoch).
	 *
	 * @return the timestamp
	 */
	public int getTimestamp() {
		return _time;
	}

	/**
	 * Gets the timestamp as a {@code Date} instance.
	 *
	 * @return the Date
	 */
	public Date getDate() {
		return new Date(_time * 1000L);
	}

	/**
	 * Gets the time of this instance, in milliseconds.
	 *
	 * @deprecated Use #getDate instead
	 * @return the time component of this ID in milliseconds
	 */
	@Deprecated
	public long getTime() {
		return _time * 1000L;
	}

	/**
	 * Gets the time of this ID, in seconds.
	 *
	 * @deprecated Use #getTimestamp instead
	 * @return the time component of this ID in seconds
	 */
	@Deprecated
	public int getTimeSecond() {
		return _time;
	}

	/**
	 * Gets the counter.
	 *
	 * @return the counter
	 * @deprecated Please use the {@link #toByteArray()} instead.
	 */
	@Deprecated
	public int getInc() {
		return _inc;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 * @deprecated Please use {@link #getTimestamp()} ()} instead.
	 */
	@Deprecated
	public int _time() {
		return _time;
	}

	/**
	 * Gets the machine identifier.
	 *
	 * @return the machine identifier
	 * @see #createFromLegacyFormat(int, int, int)
	 * @deprecated Please use {@code #toByteArray()} instead.
	 */
	@Deprecated
	public int getMachine() {
		return _machine;
	}

	/**
	 * Gets the machine identifier.
	 *
	 * @return the machine identifier
	 * @see #createFromLegacyFormat(int, int, int)
	 * @deprecated Please use {@link #toByteArray()} instead.
	 */
	@Deprecated
	public int _machine() {
		return _machine;
	}

	/**
	 * Gets the counter.
	 *
	 * @return the counter
	 * @see #createFromLegacyFormat(int, int, int)
	 * @deprecated Please use {@link #toByteArray()} instead.
	 */
	@Deprecated
	public int _inc() {
		return _inc;
	}

	/**
	 * @deprecated 'new' flag breaks the immutability of the {@code PrimaryKey}
	 *             class and will be dropped in 3.x versions of the driver
	 */
	@Deprecated
	public boolean isNew() {
		return _new;
	}

	/**
	 * @deprecated 'new' flag breaks the immutability of the {@code PrimaryKey}
	 *             class and will be dropped in 3.x versions of the driver
	 */
	@Deprecated
	public void notNew() {
		_new = false;
	}

	/**
	 * Gets the machine identifier.
	 *
	 * @return the machine identifier
	 * @see #createFromLegacyFormat(int, int, int)
	 * @deprecated
	 */
	@Deprecated
	public static int getGenMachineId() {
		return _genmachine;
	}

	/**
	 * Gets the current value of the auto-incrementing counter.
	 *
	 * @return the current counter value.
	 */
	public static int getCurrentCounter() {
		return _nextInc.get();
	}

	/**
	 * Gets the current value of the auto-incrementing counter.
	 *
	 * @deprecated Please use {@link #getCurrentCounter()} instead.
	 */
	@Deprecated
	public static int getCurrentInc() {
		return _nextInc.get();
	}

	final int _time;
	final int _machine;
	final int _inc;

	boolean _new;

	/**
	 * @deprecated This method is NOT a part of public API and will be dropped
	 *             in 3.x versions.
	 */
	@Deprecated
	public static int _flip(int x) {
		int z = 0;
		z |= ((x << 24) & 0xFF000000);
		z |= ((x << 8) & 0x00FF0000);
		z |= ((x >> 8) & 0x0000FF00);
		z |= ((x >> 24) & 0x000000FF);
		return z;
	}

	private static AtomicInteger _nextInc = new AtomicInteger((new Random()).nextInt());

	private static final int _genmachine;
	static {

		try {
			int machinePiece;
			{
				try {
					StringBuilder sb = new StringBuilder();
					Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
					while (e.hasMoreElements()) {
						NetworkInterface ni = e.nextElement();
						sb.append(ni.toString());
					}
					machinePiece = sb.toString().hashCode() << 16;
				} catch (Throwable e) {
					// exception sometimes happens with IBM JVM, use random
					LOGGER.log(Level.WARNING, e.getMessage(), e);
					machinePiece = (new Random().nextInt()) << 16;
				}
				LOGGER.fine("machine piece post: " + Integer.toHexString(machinePiece));
			}

			// add a 2 byte process piece. It must represent not only the JVM
			// but the class loader.
			// Since static var belong to class loader there could be collisions
			// otherwise
			final int processPiece;
			{
				int processId = new Random().nextInt();
				try {
					processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
				} catch (Throwable t) {
				}

				ClassLoader loader = PrimaryKey.class.getClassLoader();
				int loaderId = loader != null ? System.identityHashCode(loader) : 0;

				StringBuilder sb = new StringBuilder();
				sb.append(Integer.toHexString(processId));
				sb.append(Integer.toHexString(loaderId));
				processPiece = sb.toString().hashCode() & 0xFFFF;
				LOGGER.fine("process piece: " + Integer.toHexString(processPiece));
			}

			_genmachine = machinePiece | processPiece;
			LOGGER.fine("machine : " + Integer.toHexString(_genmachine));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
