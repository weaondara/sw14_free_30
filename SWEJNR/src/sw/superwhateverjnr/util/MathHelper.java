package sw.superwhateverjnr.util;

public class MathHelper
{
	public static double roundNumber(double number, int digits)
	{
		double pow=Math.pow(10, digits);
		return (double)Math.round(number*pow)/pow;
	}
	
	public static double absoluteDifference(double number1, double number2)
	{
		return Math.abs(Math.abs(number1) - Math.abs(number2));
	}
	
	public static boolean isInTolerance(double number1, double number2, double delta)
	{
		return (Math.abs(number1 - number2) <= delta);
	}
}
