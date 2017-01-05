import java.sql.Connection;
import java.util.Scanner;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;


/**This application connects to a database and displays a list of all customers 
 and their invoices. Each line in this report includes the customer’s email address,
 the invoice number, the invoice date, and the invoice total.
 @author Mary A. Stewart
 
 */

public class CustomerInvoiceApp {

	public static void main(String[] args) {

		System.out.println("Welcome to the Customer Invoice Report");
		getConnection();
		disconnect();

	}

	private static Connection getConnection() {
		Connection connection = null;
		try {
			// set up the home directory for Derby
			String dbDirectory = "C:/Users/MAX-Student/workspace/CustomerInvoice";
			System.setProperty("derby.system.home", dbDirectory);

			// create and return the connection
			String dbUrl = "jdbc:derby:MurachDB";
			connection = DriverManager.getConnection(dbUrl);

			// pulling info from database using sql statements
			Statement statement = connection.createStatement();
			String query = "SELECT c.EmailAddress, i.InvoiceNumber, i.InvoiceDate, "
					+ "i.InvoiceTotal FROM Customers c " + "INNER JOIN Invoices i " + "ON c.CustomerID = i.CustomerID "
					+ "ORDER BY c.EmailAddress ASC";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				String emailAddress = rs.getString("EmailAddress");
				String invoiceNumber = rs.getString("InvoiceNumber");
				Date invoiceDate = rs.getDate("InvoiceDate");
				double invoiceTotal = rs.getDouble("InvoiceTotal");

				NumberFormat currency = NumberFormat.getCurrencyInstance();

				System.out.println(emailAddress + "\t" + invoiceNumber + "   "
						+ DateFormat.getDateInstance(DateFormat.SHORT).format(invoiceDate) + "   "
						+ currency.format(invoiceTotal) + "\t");
			}
			rs.close();
			System.out.println();
		} catch (SQLException e) {
			for (Throwable t : e) {
				// e.printStackTrace();
				System.out.println(e);
			}

			return null;
		}
		return connection;
	}

	public static boolean disconnect() {
		try {
			// On a successful shutdown, this throws an exception
			String shutdownURL = "jdbc:derby:;shutdown=true";
			DriverManager.getConnection(shutdownURL);
		} catch (SQLException e) {
			if (e.getMessage().equals("Derby system shutdown.")) {
				System.out.println(e);
				return true;
			}

		}
		return false;
	}

}
