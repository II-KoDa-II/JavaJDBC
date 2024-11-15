package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataBaseController dbc = new DataBaseController();

        String customerTableQuery = "CREATE TABLE if not exists music " +
                "(id INTEGER PRIMARY KEY, name TEXT not null)";

        String customerEntryQuery = """
                INSERT INTO music (id, name)
                    select * from (VALUES (1, 'Bohemian Rhapsody'),
                       (2, 'Stairway to Heaven'),
                       (3, 'Imagine'),
                       (4, 'Sweet Child O Mine'),
                       (5, 'Hey Jude'),
                       (6, 'Hotel California'),
                       (7, 'Billie Jean'),
                       (8, 'Wonderwall'),
                       (9, 'Smells Like Teen Spirit'),
                       (10, 'Let It Be'),
                       (11, 'I Want It All'),
                       (12, 'November Rain'),
                       (13, 'Losing My Religion'),
                       (14, 'One'),
                       (15, 'With or Without You'),
                       (16, 'Sweet Caroline'),
                       (17, 'Yesterday'),
                       (18, 'Dont Stop Believin'),
                       (19, 'Crazy Train'),
                       (20, 'Always')) as new_data
                WHERE NOT EXISTS (SELECT 1 FROM music);""";

        dbc.doQuery(customerTableQuery);
        dbc.doQuery(customerEntryQuery);

        ResultSet resultSet = dbc.selectAll("music");
        System.out.println("1) Music list");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            System.out.println(id + "  " + name);
        }

        String selectWithFilter = "SELECT * FROM music WHERE LOWER(name) NOT LIKE '%m%' AND LOWER(name) NOT LIKE '%t%';";
        resultSet = dbc.customSelect(selectWithFilter);
        System.out.println("\n2) Without m and t");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            System.out.println(id + "  " + name);
        }

        String addMyMusic = "INSERT INTO music VALUES (21, 'myTestMusic')";
        dbc.doQuery(addMyMusic);
        String getMyMusic = "SELECT * FROM music WHERE id = 21";
        resultSet = dbc.customSelect(getMyMusic);
        System.out.println("\n3) Add my music");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            System.out.println(id + "  " + name);
        }

        String clientTableQuery = """
                CREATE TABLE if not exists clients (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name TEXT NOT NULL,
                    surname TEXT,
                    phone TEXT,
                    subscribed BOOLEAN
                );""";

        String booksTableQuerry = """
                CREATE TABLE if not exists books (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name TEXT NOT NULL,
                    author TEXT,
                    publishing_year INTEGER,
                    isbn TEXT,
                    publisher TEXT
                );""";

        String favoriteBooksTableQuerry = """
                CREATE TABLE if not exists favorite_books(
                client_id INTEGER,
                book_id INTEGER,
                PRIMARY KEY(client_id, book_id),
                FOREIGN KEY (client_id) REFERENCES clients(id),
                FOREIGN KEY (book_id) REFERENCES books(id)
                );""";

        dbc.doQuery(clientTableQuery);
        dbc.doQuery(booksTableQuerry);
        dbc.doQuery(favoriteBooksTableQuerry);

        String clientsEntryQuery = """
                INSERT INTO clients (name, surname, phone, subscribed)
                                    select * from (VALUES
                                       ('John', 'Doe', '123-456-7890', true),
                                       ('Jane', 'Smith', '987-654-3210', false),
                                       ('Michael', 'Johnson', '555-123-4567', true),
                                       ('Emily', 'Brown', '555-987-6543', true)
                                       ) as new_data
                                WHERE NOT EXISTS (SELECT 1 FROM clients);
                """;

        String booksEntryQuery = """
                INSERT INTO books (name, author, publishing_year, isbn, publisher)
                                    select * from (VALUES
                                       ('The Lord of the Rings', 'J.R.R. Tolkien', 1954, '0395026468', 'Allen & Unwin'),
                                       ('To Kill a Mockingbird', 'Harper Lee', 1960, '0446310759', 'HarperPerennial'),
                                       ('The Fault in Our Stars', 'John Green', 2012, '0316038746', 'Dutton'),
                                       ('The Book Thief', 'Markus Zusak', 2005, '0375831004', 'Knopf'),
                                       ('The Shack', 'William P. Young', 2007, '0316067860', 'Windblown Media'),
                                       ('The Kite Runner', 'Khaled Hosseini', 2003, '0385506982', 'Riverhead Books'),
                                       ('The Nightingale', 'Kristin Hannah', 2015, '0385387035', 'St. Martins Press'),
                                       ('Pride and Prejudice', 'Jane Austen', 1813, '0525472125', 'Penguin Classics')
                                       ) as new_data
                                WHERE NOT EXISTS (SELECT 1 FROM books);
                """;

        String favoriteBooksEntryQuerry = """
                INSERT INTO favorite_books (client_id, book_id)
                VALUES (1, 1), (1, 2), (1, 4), (2, 3), (4, 4), (3, 4), (3, 5), (2, 5)
                """;

        dbc.doQuery(clientsEntryQuery);
        dbc.doQuery(booksEntryQuery);
        dbc.doQuery(favoriteBooksEntryQuerry);

        System.out.println("\n4) new Table from clients and books\nClients:");
        String selectClientsAndBooks = """
                SELECT c.name, GROUP_CONCAT(b.name SEPARATOR ', ') AS favorite_books
                FROM clients c
                LEFT JOIN favorite_books cf ON c.id = cf.client_id
                LEFT JOIN books b ON cf.book_id = b.id
                GROUP BY c.id, c.name;""";
        resultSet = dbc.customSelect(selectClientsAndBooks);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            Array fb = resultSet.getArray("favorite_books");
            System.out.println(name + " " + fb);
        }

        String selectBooksWithYearSort = "SELECT * FROM books ORDER BY publishing_year ASC;";
        resultSet = dbc.customSelect(selectBooksWithYearSort);
        System.out.println("\n5) Books sorted by year");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int year = resultSet.getInt("publishing_year");
            System.out.println(name + " " + year);
        }

        String selectBooksSorted = "SELECT * FROM books WHERE publishing_year > 2000;";
        resultSet = dbc.customSelect(selectBooksSorted);
        System.out.println("\n6) Books published after 2000");
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int year = resultSet.getInt("publishing_year");
            System.out.println( name + " " + year);
        }

        String meToClientQuery = """
                INSERT INTO clients (name, surname, phone, subscribed)
                VALUES ('Daniil', 'Koff', '89132863505', false)
                """;

        String myBooksToBooksQuery = """
                INSERT INTO books (name, author, publishing_year, isbn, publisher)
                VALUES ('Foul Detective Satori', 'Zun', 2019, '4049133482', 'Kadokawa'),
                 ('Curiosities of Lotus Asia', 'Zun', 2010, '4048685015', 'ASCII Media Works')
                """;

        String myFavoriteBooksQuery = """
                INSERT INTO favorite_books (client_id, book_id)
                VALUES (5, 9), (5, 10)
                """;

        dbc.doQuery(meToClientQuery);
        dbc.doQuery(myBooksToBooksQuery);
        dbc.doQuery(myFavoriteBooksQuery);

        System.out.println("\n7) Me and my books");
        String selectMeAndMyBooks = """
                SELECT c.name, GROUP_CONCAT(b.name SEPARATOR ', ') AS favorite_books
                FROM clients c
                LEFT JOIN favorite_books cf ON c.id = cf.client_id
                LEFT JOIN books b ON cf.book_id = b.id
                where c.id = 5
                GROUP BY c.id, c.name;""";
        resultSet = dbc.customSelect(selectMeAndMyBooks);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            Array fb = resultSet.getArray("favorite_books");
            System.out.println(name + " " + fb);
        }

        //8) Удаление таблиц посетителей и книг
        dbc.dropTable("favorite_books");
        dbc.dropTable("clients");
        dbc.dropTable("books");
    }
}