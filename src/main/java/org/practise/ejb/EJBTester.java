package org.practise.ejb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.practise.ejb.service.LibraryPersistentBeanRemote;

public class EJBTester {

	BufferedReader brConsoleReader = null;

	public static void main(String[] args) throws Exception {

		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");

		props.setProperty("org.omg.CORBA.ORBInitialPort", "51037");

		final InitialContext context = new InitialContext(props);

		EJBTester ejbTester = new EJBTester();

		ejbTester.testEntityEjb(context);
	}

	private void showGUI() {
		System.out.println("**********************");
		System.out.println("Welcome to Book Store");
		System.out.println("**********************");
		System.out.print("Options \n1. Add Book\n2. Exit \nEnter Choice: ");

		brConsoleReader = new BufferedReader(new InputStreamReader(System.in));
	}

	private void testEntityEjb(InitialContext ctx) {

		try {
			int choice = 1;

			LibraryPersistentBeanRemote libraryBean = (LibraryPersistentBeanRemote) ctx
					.lookup("java:global/ejb-persistence-ear-1.0/ejb-spec-impl-1.0/LibraryPersistentBean");

			libraryBean.getBooks();
			while (choice != 2) {
				String bookName;
				showGUI();
				String strChoice = brConsoleReader.readLine();
				choice = Integer.parseInt(strChoice);
				if (choice == 1) {
					System.out.print("Enter book name: ");
					bookName = brConsoleReader.readLine();
					libraryBean.addBook(bookName);
				} else if (choice == 2) {
					break;
				}
			}

			List<String> booksList = libraryBean.getBooks();

			System.out.println("Book(s) entered so far: " + booksList.size());
			int i = 0;
			for (String bookName : booksList) {
				System.out.println((i + 1) + ". " + bookName);
				i++;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (brConsoleReader != null) {
					brConsoleReader.close();
				}
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

}
