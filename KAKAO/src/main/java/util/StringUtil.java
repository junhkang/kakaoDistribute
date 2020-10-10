package util;

public class StringUtil {


	public static String checkParseNull(Object obj) {
		String result = "";
		if (null != obj) {
			result = String.valueOf(obj);
			if (result.contains("["))	{
				result = result.substring(1,result.length());
			}
			if (result.contains("]"))	{
				result = result.substring(0,result.length()-1);
			}
			if (result.contains("\""))	{
				result = result.substring(1,result.length()-1);
			}
		}
		return result;
	}
	public static String checkParseLong(Object obj) {
		String result = "";
	
		if (null != obj) {
			result = String.valueOf(obj);
			try	{
				if (result.contains("["))	{
					result = result.substring(1,result.length());
				}
				if (result.contains("]"))	{
					result = result.substring(0,result.length()-1);
				}
				long L = Long.valueOf(result);
				if (L < 0)	{
					throw new Exception("금액/인원 값 음수체크");
				}
			}	catch (Exception e)	{
				result = "";
			}
		}
		return result;
	}
	
	public static String getRandomToken(int n) { 
  
        String tokenTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
            int index = (int)(tokenTable.length() * Math.random()); 
            sb.append(tokenTable.charAt(index)); 
        } 
  
        return sb.toString(); 
    } 
}
