/**
 * ģ��ALU���������͸���������������
 * 
 * @author [151250174_���]
 *
 */

public class ALU {

	/**
	 * ����ʮ���������Ķ����Ʋ����ʾ��<br/>
	 * ����integerRepresentation("9", 8)
	 * 
	 * @param number
	 *            ʮ������������Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length
	 *            �����Ʋ����ʾ�ĳ���
	 * @return number�Ķ����Ʋ����ʾ������Ϊlength
	 */
	public String integerRepresentation(String number, int length) {
		int decNumber = Integer.parseInt(number);
		String[] temp = new String[length];
		String result = new String();
		// ��ʮ������Ϊ0ʱ
		if (decNumber == 0) {
			for (int i = 0; i < length; i++) {
				temp[i] = "0";
			}
			for (int i = 0; i < length; i++) {
				result += temp[i];
			}
		}
		// ��ʮ����������0ʱ
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
		// ��ʮ������С��0ʱ
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
	 * ����ʮ���Ƹ������Ķ����Ʊ�ʾ�� ��Ҫ���� 0������񻯡����������+Inf���͡�-Inf������ NaN�����أ������� IEEE 754��
	 * �������Ϊ��0���롣<br/>
	 * ����floatRepresentation("11.375", 8, 11)
	 * 
	 * @param number
	 *            ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return number�Ķ����Ʊ�ʾ������Ϊ 1+eLength+sLength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
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
		// ��ʼ�����
		for (int i = 0; i < results.length; i++) {
			results[i] = "0";
		}
		// ��������Ƿ�Ϊ����
		for (int i = 0; i < number.length(); i++) {
			if ((!Character.isDigit(number.charAt(i))) && (number.charAt(i) != '.') && (number.charAt(i) != '-')) {
				return "NaN";
			}
		}
		// ����
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
		// ָ������
		if ((dot - newdot + bias) > ((int) Math.pow(base, eLength) - 1)) {
			for (int i = 0; i < eLength; i++) {
				results[i + 1] = "1";
			}
			for (int i = eLength + 1; i < 1 + eLength + sLength; i++) {
				results[i] = "0";
			}
		}
		// ָ������,�����
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
		// ���
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

		// �����ַ���
		for (int i = 0; i < results.length; i++) {
			result += results[i];
		}
		return result;
	}

	/**
	 * ����ʮ���Ƹ�������IEEE 754��ʾ��Ҫ�����{@link #floatRepresentation(String, int, int)
	 * floatRepresentation}ʵ�֡�<br/>
	 * ����ieee754("11.375", 32)
	 * 
	 * @param number
	 *            ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length
	 *            �����Ʊ�ʾ�ĳ��ȣ�Ϊ32��64
	 * @return number��IEEE 754��ʾ������Ϊlength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String ieee754(String number, int length) {
		String result = "";
		// 32λ������
		if (length == 32) {
			result = floatRepresentation(number, 8, 23);
		}
		// 64λ˫����
		else if (length == 64) {
			result = floatRepresentation(number, 11, 52);
		} else {
			result = "Not ieee754";
		}
		return result;
	}

	/**
	 * ��������Ʋ����ʾ����������ֵ��<br/>
	 * ����integerTrueValue("00001001")
	 * 
	 * @param operand
	 *            �����Ʋ����ʾ�Ĳ�����
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 */
	public String integerTrueValue(String operand) {
		int result = 0;
		// �����һλΪ0��������ֵΪ��������
		if (operand.substring(0, 1).equals("0")) {
			for (int i = operand.length(); i > 0; i--) {
				if (operand.substring(i - 1, i).equals("1")) {
					result += Math.pow(2, operand.length() - i);
				}
			}
			return result + "";
		}
		// �����һλΪ1��������ֵΪ����
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
	 * ���������ԭ���ʾ�ĸ���������ֵ��<br/>
	 * ����floatTrueValue("01000001001101100000", 8, 11)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ����������ֱ��ʾΪ��+Inf���͡�-Inf����
	 *         NaN��ʾΪ��NaN��
	 */
	public String floatTrueValue(String operand, int eLength, int sLength) {
		String result = "";
		int base = 2;
		int bias =(int)Math.pow(base, eLength-1)-1;
		int exponent = 0;
		double temp = 0.0;
		//�и����
		String[] num = new String[3];
		num[0] = operand.substring(0,1);
		num[1] = operand.substring(1,eLength+1);
		num[2] = operand.substring(eLength+1,operand.length());
		//����Ƿ�ΪNaN
		if((!num[1].contains("0"))&&(num[2].contains("1"))){
			return "NaN";
		}
		//����Ƿ�Ϊ����
		else if ((!num[1].contains("0"))&&(!num[2].contains("1"))) {
			if(num[0].equals("0")){
				return "+inf";
			}
			else {
				return "-inf";
			}
		}
		//����Ƿ�Ϊ0
		else if((!num[1].contains("1"))&&(!num[2].contains("1"))){
			return "0";
		}
		//��鷴���
		else if (!num[1].contains("1")) {
			
		}
		//���
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
	 * ��λȡ��������<br/>
	 * ����negation("00001001")
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @return operand��λȡ���Ľ��
	 */
	public String negation(String operand) {
		String result = "";
		for (int i = 0; i < operand.length(); i++) {
			result += notGate(operand.charAt(i));
		}
		return result;
	}

	/**
	 * ���Ʋ�����<br/>
	 * ����leftShift("00001001", 2)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param n
	 *            ���Ƶ�λ��
	 * @return operand����nλ�Ľ��
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
	 * �߼����Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param n
	 *            ���Ƶ�λ��
	 * @return operand�߼�����nλ�Ľ��
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
	 * �������Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param n
	 *            ���Ƶ�λ��
	 * @return operand��������nλ�Ľ��
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
	 * ȫ����������λ�Լ���λ���мӷ����㡣<br/>
	 * ����fullAdder('1', '1', '0')
	 * 
	 * @param x
	 *            ��������ĳһλ��ȡ0��1
	 * @param y
	 *            ������ĳһλ��ȡ0��1
	 * @param c
	 *            ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ��ӵĽ�����ó���Ϊ2���ַ�����ʾ����1λ��ʾ��λ����2λ��ʾ��
	 */
	public String fullAdder(char x, char y, char c) {
		String result = "";
		String S = xorGate(xorGate(x, y),c)+"";
		String C = orGate(andGate(x, y), orGate(andGate(x, c), andGate(y, c)))+"";
		result = C+S;
		return	result;
	}

	/**
	 * 4λ���н�λ�ӷ�����Ҫ�����{@link #fullAdder(char, char, char) fullAdder}��ʵ��<br/>
	 * ����claAdder("1001", "0001", '1')
	 * 
	 * @param operand1
	 *            4λ�����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            4λ�����Ʊ�ʾ�ļ���
	 * @param c
	 *            ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ����Ϊ5���ַ�����ʾ�ļ����������е�1λ�����λ��λ����4λ����ӽ�������н�λ��������ѭ�����
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
	 * ��һ����ʵ�ֲ�������1�����㡣 ��Ҫ�������š����š�����ŵ�ģ�⣬ ������ֱ�ӵ���
	 * {@link #fullAdder(char, char, char) fullAdder}��
	 * {@link #claAdder(String, String, char) claAdder}��
	 * {@link #adder(String, String, char, int) adder}��
	 * {@link #integerAddition(String, String, int) integerAddition}������<br/>
	 * ����oneAdder("00001001")
	 * 
	 * @param operand
	 *            �����Ʋ����ʾ�Ĳ�����
	 * @return operand��1�Ľ��������Ϊoperand�ĳ��ȼ�1�����е�1λָʾ�Ƿ���������Ϊ1������Ϊ0��������λΪ��ӽ��
	 */
	public String oneAdder(String operand) {
		
		return null;
	}

	/**
	 * �ӷ�����Ҫ�����{@link #claAdder(String, String, char)}����ʵ�֡�<br/>
	 * ����adder("0100", "0011", ��0��, 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ļ���
	 * @param c
	 *            ���λ��λ
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String adder(String operand1, String operand2, char c, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �����ӷ���Ҫ�����{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerAddition("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ļ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String integerAddition(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �����������ɵ���{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerSubtraction("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ļ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ��������
	 */
	public String integerSubtraction(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �����˷���ʹ��Booth�㷨ʵ�֣��ɵ���{@link #adder(String, String, char, int) adder}�ȷ�����
	 * <br/>
	 * ����integerMultiplication("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ĳ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ����˽�������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����˽��
	 */
	public String integerMultiplication(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �����Ĳ��ָ������������ɵ���{@link #adder(String, String, char, int) adder}�ȷ���ʵ�֡�<br/>
	 * ����integerDivision("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ĳ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ�ڸ�λ������λ
	 * @return ����Ϊ2*length+1���ַ�����ʾ�������������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0�������lengthλΪ�̣�
	 *         ���lengthλΪ����
	 */
	public String integerDivision(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �����������ӷ������Ե���{@link #adder(String, String, char, int) adder}�ȷ�����
	 * ������ֱ�ӽ�������ת��Ϊ�����ʹ��{@link #integerAddition(String, String, int)
	 * integerAddition}�� {@link #integerSubtraction(String, String, int)
	 * integerSubtraction}��ʵ�֡�<br/>
	 * ����signedAddition("1100", "1011", 8)
	 * 
	 * @param operand1
	 *            ������ԭ���ʾ�ı����������е�1λΪ����λ
	 * @param operand2
	 *            ������ԭ���ʾ�ļ��������е�1λΪ����λ
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ����������ţ�����ĳ���������ĳ���С��lengthʱ��
	 *            ��Ҫ���䳤����չ��length
	 * @return ����Ϊlength+2���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������2λΪ����λ��
	 *         ��lengthλ����ӽ��
	 */
	public String signedAddition(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �������ӷ����ɵ���{@link #signedAddition(String, String, int) signedAddition}
	 * �ȷ���ʵ�֡�<br/>
	 * ����floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ļ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength
	 *            ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����ӽ�������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0����
	 *         ����λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatAddition(String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �������������ɵ���{@link #floatAddition(String, String, int, int, int)
	 * floatAddition}����ʵ�֡�<br/>
	 * ����floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ļ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength
	 *            ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ�������������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0����
	 *         ����λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatSubtraction(String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �������˷����ɵ���{@link #integerMultiplication(String, String, int)
	 * integerMultiplication}�ȷ���ʵ�֡�<br/>
	 * ����floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ĳ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0����
	 *         ����λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatMultiplication(String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �������������ɵ���{@link #integerDivision(String, String, int) integerDivision}
	 * �ȷ���ʵ�֡�<br/>
	 * ����floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ĳ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0����
	 *         ����λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatDivision(String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	// ����
	private char notGate(char s) {
		if (s=='1') {
			return '0';
		} else {
			return '1';
		}
	}

	// ����
	private char andGate(char s1, char s2) {
		if (s1=='1'&& s2=='1') {
			return '1';
		} else {
			return '0';
		}
	}

	// ����
	private char orGate(char s1, char s2) {
		if (s1=='1' || s2=='1') {
			return '1';
		} else {
			return '0';
		}
	}

	// �����
	private char xorGate(char x, char y) {
		if (x==y) {
			return '0';
		} else {
			return '1';
		}
	}
	
	/**
	 * �����޷�����������ֵ
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
