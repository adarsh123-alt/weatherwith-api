package myPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String apikey="aae92232e1adb45fb772b5d76c1f23b1";
		// city value get kar raha hua index.jsp se
		String city= request.getParameter("city");
		// string mein url java ko nahi pata ye kya hai java ke liye ye string hai bs
		String apiUrl="https://api.openweathermap.org/data/2.5/weather?q=New%20Delhi&appid=aae92232e1adb45fb772b5d76c1f23b1";
		// ye ek url object chiye url samjhne ke liye isliye url ka object hai
		URL url = new URL(apiUrl);
		// http se connection http type ka hota hai an actual ye online internet se connect ab connection open hai
		HttpURLConnection connection =(HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		//reading
		// inputStream hai ye data ko byte store karta hai
		InputStream inputStream=connection.getInputStream();
		// reader hai jo data ko read karta hai jo 0 and 1 data ko read karne mein kaam aata hai
		InputStreamReader  reader = new InputStreamReader(inputStream) ;
		// store in string
		// String mutable hota not change value use string builder
		StringBuilder responseContent = new StringBuilder();
		// scanner only read karta hai 
		Scanner scanner=new Scanner(reader);
		while(scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}
		scanner.close();
		Gson gson=new Gson();
		JsonObject jsonObject= gson.fromJson(responseContent.toString(),JsonObject.class);
		
		long dateTimestap=jsonObject.get("dt").getAsLong()*1000;
		String date=new Date(dateTimestap).toString();
		
		double temperatureKelvin=jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius = (int)(temperatureKelvin -275);
		
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		double windSpeed= jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperatureCelsius",temperatureCelsius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity",humidity);
		request.setAttribute("windSpeed",windSpeed);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}

}
