package org.example

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

fun main() {
    // File path:
    val fileName = "mockdata.csv"
    val url = "jdbc:sqlite::memory:"

    val connection: Connection = DriverManager.getConnection(url)

    createAndPopulateInMemoryDatabase(connection)

    if (connection != null) { readFromInMemoryDatabase(connection) }

    //val csvReader = CsvReader()

    //csvReader.readCsvAndInsert(fileName)

}

fun createAndPopulateInMemoryDatabase(connection: Connection) {
    val createTableSQL = """ CREATE TABLE IF NOT EXISTS people ( 
        |id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, 
        |age INTEGER, city TEXT ); """.trimMargin()
    val insertDataSQL = """ INSERT INTO people (name, age, city) 
        |VALUES ('Alice', 30, 'George'), 
        |('Bob', 25, 'Cape Town'), 
        |('Charlie', 28, 'Johannesburg'), 
        |('Diana', 32, 'Durban'), 
        |('Edward', 27, 'Pretoria'), 
        |('Fiona', 29, 'Port Elizabeth'), 
        |('George', 34, 'Bloemfontein'), 
        |('Helen', 26, 'East London'), 
        |('Ian', 31, 'Pietermaritzburg'), 
        |('Julia', 33, 'Stellenbosch'); """.trimMargin()
    try {
        //val connection: Connection = DriverManager.getConnection(url)
        connection.createStatement().use { statement ->
            statement.execute(createTableSQL)
            statement.execute(insertDataSQL)
            println("In-memory database and table created, data inserted successfully.")
        }
    } catch (e: SQLException) {
    }
}

fun readFromInMemoryDatabase(connection: Connection) {
    val querySQL = "SELECT * FROM people"
    try { connection.createStatement().use {
            statement -> val resultSet: ResultSet = statement.executeQuery(querySQL)
        while (resultSet.next()) {
            val id = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val age = resultSet.getInt("age")
            val city = resultSet.getString("city")
            println("id: $id, name: $name, age: $age, city: $city")
        }
    }
    } catch (e: SQLException) {
        println(e.message)
    }
}