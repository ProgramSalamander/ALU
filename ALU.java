/**
 * 模拟ALU进行整数和浮点数的四则运算
 * 
 * @author [151250174_徐杨晨]
 *
 */

public class ALU {

	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * 
	 * @param number
	 *            十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length
	 *            二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation(String number, int length) {
		int decNumber = Integer.parseInt(number);
		String[] temp = new String[length];
		String result = new String();
		// 当十进制数为0时
		if (decNumber == 0) {
			for (int i = 0; i < length; i++) {
				temp[i] = "0";
			}
			for (int i = 0; i < length; i++) {
				result += temp[i];
			}
		}
		// 当十进制数大于0时
		else if (decNumber > 0) {
			temp[0] = "0";
			for (int i = 1; i < length; i++) {
				if (Math.pow(2, length - i - 1) <= decNumber) {
					temp[i] = "1";
					decNumber -= Math.pow(2, length - i - 1);
				} else {
					temp[i] = "0";
				}
			}
			for (int i = 0; i < length; i++) {
				result += temp[i];
			}
		}
		// 当十进制数小于0时
		else {
			temp[0] = "1";
			for (int i = 1; i < length; i++) {
				if ((-Math.pow(2, length - 1) + Math.pow(2, length - i - 1)) <= decNumber) {
					temp[i] = "1";
					decNumber -= Math.pow(2, length - i - 1);
				} else {
					temp[i] = "0";
				}
			}
			for (int i = 0; i < length; i++) {
				result += temp[i];
			}
		}
		return result;
	}

	/**
	 * 生成十进制浮点数的二进制表示。 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * 
	 * @param number
	 *            十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation(String number, int eLength, int sLength) {
		int totalLength = 1 + eLength + sLength;
		int base = 2;
		int bias = (int) (Math.pow(2, eLength - 1)) - 1;
		int exponent = 0;
		int dot = 0;
		String[] results = new String[totalLength];
		String result = "";
		String temp = "";
		// 初始化结果
		for (int i = 0; i < results.length; i++) {
			results[i] = "0";
		}
		// 检查输入是否为数字
		for (int i = 0; i < number.length(); i++) {
			if ((!Character.isDigit(number.charAt(i))) && (number.charAt(i) != '.') && (number.charAt(i) != '-')) {
				return "NaN";
			}
		}
		// 负数
		if (number.charAt(0) == '-') {
			results[0] = "1";
		}

		String[] num = number.split("\\.");
		String dec = "0." + num[1];
		double d = Double.parseDouble(dec);
		int intNumber = Math.abs(Integer.parseInt(num[0]));
		int length = 0;
		for (int i = 0; i < eLength; i++) {
			if (intNumber >= (int) Math.pow(base, i)) {
				length = i + 1;
			}
		}
		temp += integerRepresentation(intNumber + "", length + 2).substring(1);
		totalLength -= temp.length();
		dot = temp.length();
		for (int i = 0; i < totalLength + 1; i++) {
			if ((d * 2) >= 1.0) {
				temp += "1";
				d = d * 2 - 1.0;
			} else {
				temp += "0";
				d = d * 2;
			}
		}
		int newdot = 0;
		for (int i = 0; i < dot; i++) {
			if (temp.charAt(i) == '1') {
				newdot = i + 1;
				break;
			}
		}
		// 指数上溢
		if ((dot - newdot + bias) > ((int) Math.pow(base, eLength) - 1)) {
			for (int i = 0; i < eLength; i++) {
				results[i + 1] = "1";
			}
			for (int i = eLength + 1; i < 1 + eLength + sLength; i++) {
				results[i] = "0";
			}
		}
		// 指数下溢,反规格化
		else if (dot - newdot + bias < 0) {
			exponent = -((int) Math.pow(base, eLength - 1) - 1);
			newdot = dot + bias - exponent;
			for (int i = 0; i < eLength; i++) {
				results[i + 1] = "0";
			}
			for (int i = eLength + 1; i < 1 + eLength + sLength; i++) {
				results[i] = temp.substring(newdot, newdot + 1);
				newdot++;
			}
		}
		// 规格化
		else {
			exponent = dot - newdot + bias;
			for (int i = 0; i < eLength; i++) {
				results[i + 1] = integerRepresentation(exponent + "", eLength + 1).substring(1).charAt(i) + "";
			}
			for (int i = eLength + 1; i < 1 + eLength + sLength; i++) {
				results[i] = temp.substring(newdot, newdot + 1);
				newdot++;
			}
		}

		// 生成字符串
		for (int i = 0; i < results.length; i++) {
			result += results[i];
		}
		return result;
	}

	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int)
	 * floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * 
	 * @param number
	 *            十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length
	 *            二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754(String number, int length) {
		String result = "";
		// 32位单精度
		if (length == 32) {
			result = floatRepresentation(number, 8, 23);
		}
		// 64位双精度
		else if (length == 64) {
			result = floatRepresentation(number, 11, 52);
		} else {
			result = "Not ieee754";
		}
		return result;
	}

	/**
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * 
	 * @param operand
	 *            二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue(String operand) {
		int result = 0;
		// 补码第一位为0，代表真值为正数或零
		if (operand.substring(0, 1).equals("0")) {
			for (int i = operand.length(); i > 0; i--) {
				if (operand.substring(i - 1, i).equals("1")) {
					result += Math.pow(2, operand.length() - i);
				}
			}
			return result + "";
		}
		// 补码第一位为1，代表真值为负数
		else {
			result = (int) -Math.pow(2, operand.length() - 1);
			for (int i = 1; i < operand.length(); i++) {
				if (operand.substring(i, i + 1).equals("1")) {
					result += Math.pow(2, operand.length() - i - 1);
				}
			}
			return result + "";
		}
	}

	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”，
	 *         NaN表示为“NaN”
	 */
	public String floatTrueValue(String operand, int eLength, int sLength) {
		String result = "";
		int base = 2;
		int bias =(int)Math.pow(base, eLength-1)-1;
		int exponent = 0;
		double temp = 0.0;
		//切割浮点数
		String[] num = new String[3];
		num[0] = operand.substring(0,1);
		num[1] = operand.substring(1,eLength+1);
		num[2] = operand.substring(eLength+1,operand.length());
		//检查是否为NaN
		if((!num[1].contains("0"))&&(num[2].contains("1"))){
			return "NaN";
		}
		//检查是否为无穷
		else if ((!num[1].contains("0"))&&(!num[2].contains("1"))) {
			if(num[0].equals("0")){
				return "+inf";
			}
			else {
				return "-inf";
			}
		}
		//检查是否为0
		else if((!num[1].contains("1"))&&(!num[2].contains("1"))){
			return "0";
		}
		//检查反规格化
		else if (!num[1].contains("1")) {
			
		}
		//规格化
		else {
			if(num[0].equals("1")){
				result+="-";
			}
			temp = 1.0;
			exponent = Integer.parseInt(integerTrueValue1(num[1]))-bias;
			for(int i=0;i<sLength;i++){
				if(num[2].charAt(i)=='1')
					temp += Math.pow(base, -i-1);
			}
			temp = temp * Math.pow(base,exponent);
			result += temp+"";
		}
		
		return result;
	}

	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation(String operand) {
		String result = "";
		for (int i = 0; i < operand.length(); i++) {
			result += notGate(operand.charAt(i));
		}
		return result;
	}

	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            左移的位数
	 * @return operand左移n位的结果
	 */
	public String leftShift(String operand, int n) {
		String[] op = new String[operand.length()];
		for (int i = 0; i < operand.length(); i++) {
			op[i] = operand.substring(i, i + 1);
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < operand.length() - 1; j++) {
				op[j] = op[j + 1];
			}
		}
		for (int i = 0; i < n; i++) {
			op[operand.length() - i - 1] = "0";
		}
		operand = "";
		for (int i = 0; i < op.length; i++) {
			operand += op[i];
		}
		return operand;
	}

	/**
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            右移的位数
	 * @return operand逻辑右移n位的结果
	 */
	public String logRightShift(String operand, int n) {
		String[] op = new String[operand.length()];
		for (int i = 0; i < operand.length(); i++) {
			op[i] = operand.substring(i, i + 1);
		}
		for (int i = 0; i < n; i++) {
			for (int j = operand.length() - 1; j > 0; j--) {
				op[j] = op[j - 1];
			}
		}
		for (int i = 0; i < n; i++) {
			op[i] = "0";
		}
		operand = "";
		for (int i = 0; i < op.length; i++) {
			operand += op[i];
		}
		return operand;
	}

	/**
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            右移的位数
	 * @return operand算术右移n位的结果
	 */
	public String ariRightShift(String operand, int n) {
		String[] op = new String[operand.length()];
		for (int i = 0; i < operand.length(); i++) {
			op[i] = operand.substring(i, i + 1);
		}
		for (int i = 0; i < n; i++) {
			for (int j = operand.length() - 1; j > 0; j--) {
				op[j] = op[j - 1];
			}
		}
		if (operand.charAt(0) == '0') {
			for (int i = 0; i < n; i++) {
				op[i] = "0";
			}
		} else {
			for (int i = 0; i < n; i++) {
				op[i] = "1";
			}
		}

		operand = "";
		for (int i = 0; i < op.length; i++) {
			operand += op[i];
		}
		return operand;
	}

	/**
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * 
	 * @param x
	 *            被加数的某一位，取0或1
	 * @param y
	 *            加数的某一位，取0或1
	 * @param c
	 *            低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
	 */
	public String fullAdder(char x, char y, char c) {
		String result = "";
		String S = xorGate(xorGate(x, y),c)+"";
		String C = orGate(andGate(x, y), orGate(andGate(x, c), andGate(y, c)))+"";
		result = C+S;
		return	result;
	}

	/**
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * 
	 * @param operand1
	 *            4位二进制表示的被加数
	 * @param operand2
	 *            4位二进制表示的加数
	 * @param c
	 *            低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder(String operand1, String operand2, char c) {
		String result = "";
		char x1 = operand1.charAt(3);
		char x2 = operand1.charAt(2);
		char x3 = operand1.charAt(1);
		char x4 = operand1.charAt(0);
		char y1 = operand2.charAt(3);
		char y2 = operand2.charAt(2);
		char y3 = operand2.charAt(1);
		char y4 = operand2.charAt(0);
		char p1 = orGate(x1, y1);
		char p2 = orGate(x2, y2);
		char p3 = orGate(x3, y3);
		char p4 = orGate(x4, y4);
		char g1 = andGate(x1, y1);
		char g2 = andGate(x2, y2);
		char g3 = andGate(x3, y3);
		char g4 = andGate(x4, y4);
		char c1 = orGate(g1,andGate(p1, c));
		char c2 = orGate(orGate(g2, andGate(p2, g1)),andGate(andGate(p2, p1), c));
		char c3 = orGate(g3, orGate(andGate(p3, g2), orGate(andGate(p3, andGate(p2, g1)), andGate(p3, andGate(p2, andGate(p1, c))))));
		char c4 = orGate(g4, orGate(andGate(p4, g3), orGate(andGate(p4, andGate(p3, g2)),orGate(andGate(p4, andGate(p3, andGate(p2, g1))), andGate(p4, andGate(p3, andGate(p2, andGate(p1, c))))))));
		result = c4+"" + fullAdder(x4, y4, c3).charAt(1)+""+ fullAdder(x3, y3, c2).charAt(1)+""+fullAdder(x2, y2, c1).charAt(1)+""+fullAdder(x1, y1, c).charAt(1)+"";
		return result;
	}

	/**
	 * 加一器，实现操作数加1的运算。 需要采用与门、或门、异或门等模拟， 不可以直接调用
	 * {@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 * 
	 * @param operand
	 *            二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder(String operand) {
		
		return null;
	}

	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被加数
	 * @param operand2
	 *            二进制补码表示的加数
	 * @param c
	 *            最低位进位
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，
	 *            需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder(String operand1, String operand2, char c, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被加数
	 * @param operand2
	 *            二进制补码表示的加数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，
	 *            需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被减数
	 * @param operand2
	 *            二进制补码表示的减数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，
	 *            需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。
	 * <br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被乘数
	 * @param operand2
	 *            二进制补码表示的乘数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，
	 *            需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被除数
	 * @param operand2
	 *            二进制补码表示的除数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，
	 *            需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，
	 *         最后length位为余数
	 */
	public String integerDivision(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int)
	 * integerAddition}、 {@link #integerSubtraction(String, String, int)
	 * integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * 
	 * @param operand1
	 *            二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2
	 *            二进制原码表示的加数，其中第1位为符号位
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，
	 *            需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，
	 *         后length位是相加结果
	 */
	public String signedAddition(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int) signedAddition}
	 * 等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被加数
	 * @param operand2
	 *            二进制表示的加数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @param gLength
	 *            保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），
	 *         其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatAddition(String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int)
	 * floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被减数
	 * @param operand2
	 *            二进制表示的减数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @param gLength
	 *            保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），
	 *         其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction(String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int)
	 * integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被乘数
	 * @param operand2
	 *            二进制表示的乘数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），
	 *         其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication(String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}
	 * 等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被除数
	 * @param operand2
	 *            二进制表示的除数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），
	 *         其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision(String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	// 非门
	private char notGate(char s) {
		if (s=='1') {
			return '0';
		} else {
			return '1';
		}
	}

	// 与门
	private char andGate(char s1, char s2) {
		if (s1=='1'&& s2=='1') {
			return '1';
		} else {
			return '0';
		}
	}

	// 或门
	private char orGate(char s1, char s2) {
		if (s1=='1' || s2=='1') {
			return '1';
		} else {
			return '0';
		}
	}

	// 异或门
	private char xorGate(char x, char y) {
		if (x==y) {
			return '0';
		} else {
			return '1';
		}
	}
	
	/**
	 * 计算无符号整数的真值
	 * @param number
	 * @return
	 */
	public String integerTrueValue1(String operand){
		int result =0;
		for (int i = operand.length(); i > 0; i--) {
			if (operand.substring(i - 1, i).equals("1")) {
				result += Math.pow(2, operand.length() - i);
			}
		}
		return result+"";
	}
	public static void main(String[] args) {
		ALU alu = new ALU();
		System.out.println(alu.claAdder("1111", "1111", '1'));
	}
}
