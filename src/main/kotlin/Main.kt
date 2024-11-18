package org.example

import java.io.BufferedReader
import java.io.InputStreamReader
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

fun main() {
    // File path:
    val fileName = "mockdata.csv"

    val url = "jdbc:sqlite::memory:"

    val connection: Connection = DriverManager.getConnection(url)

    createInMemoryDatabase(connection)

    readTextFile()

    if (connection != null) {
        readFromInMemoryDatabase(connection)
    }

    //val csvReader = CsvReader()

    //csvReader.readCsvAndInsert(fileName)

}

fun readTextFile() {
    // Obtain the ClassLoader for the current thread
    val classLoader = Thread.currentThread().contextClassLoader
    // Get the InputStream for the file
    val inputStream = classLoader.getResourceAsStream("acrecords.txt")
    // Ensure the InputStream is not null
    if (inputStream != null) {
        // Use BufferedReader to read the file line by line
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines {
                lines -> lines.forEach {
                line -> println(line)
            }
        }
    } else {
        println("File not found!")
    }
}

fun populateInMemoryDB(connection: Connection, line: String){
    //Person Name,Time,Device,Event,Status
    var lineArray = line.split(",")
    val insertDataSQL = """ INSERT INTO acrecords (name, time, device, event, status) 
        |VALUES (${lineArray.get(0)}, ${lineArray.get(1)}, ${lineArray.get(2)}, ${lineArray.get(3)}, ${lineArray.get(4).toInt()}); """.trimMargin()
    try {
        //val connection: Connection = DriverManager.getConnection(url)
        connection.createStatement().use { statement ->
            statement.execute(insertDataSQL)
            println("In-memory data inserted successfully.")
        }
    } catch (e: SQLException) {
    }
}

fun createInMemoryDatabase(connection: Connection) {
    //Person Name,Time,Device,Event,Status
    val createTableSQL = """ CREATE TABLE IF NOT EXISTS acrecords ( 
        |id INTEGER PRIMARY KEY AUTOINCREMENT, 
        |name TEXT, 
        |time TEXT, 
        |device TEXT
        |event TEXT 
        |status INTEGER ); """.trimMargin()
    try {
        //val connection: Connection = DriverManager.getConnection(url)
        connection.createStatement().use { statement ->
            statement.execute(createTableSQL)
            //statement.execute(insertDataSQL)
            println("In-memory database and table created.")
        }
    } catch (e: SQLException) {
    }
}

fun readFromInMemoryDatabase(connection: Connection) {
    val querySQL = "SELECT * FROM acrecords"
    try { connection.createStatement().use {
            statement -> val resultSet: ResultSet = statement.executeQuery(querySQL)
        /**
         * |id INTEGER ,
         |name TEXT,
         |time TEXT,
         |device TEXT
         |event TEXT
         |status INTEGER
         */
        while (resultSet.next()) {
            val id = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val time = resultSet.getInt("time")
            val device = resultSet.getString("device")
            val event = resultSet.getInt("event")
            val status = resultSet.getInt("status")
            println("id: $id, name: $name, time: $time, device: $device, event: $event, status: $status")
        }
    }
    } catch (e: SQLException) {
        println(e.message)
    }
}