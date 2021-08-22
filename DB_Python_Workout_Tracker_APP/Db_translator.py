import mysql.connector
from mysql.connector import errorcode

class Translator():
    def __init__(self):
        self.host=""
        self.user=""
        self.database=""

        # connection
        self.connection = None
        self.cursor = None

    def connect(self, host, username, password, db):
        try:
            self.connection = mysql.connector.connect(
                host=host,
                user=username,
                passwd=password,
                database=db
            )
            self.cursor = self.connection.cursor()
            print("CONNECTION SUCCESSFUL")
            #self.get()

        except mysql.connector.Error as err:
            if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
                print("Something is wrong with your user name or password")
            elif err.errno == errorcode.ER_BAD_DB_ERROR:
                print("Database does not exist")
            else:
                print(err)
            return 0 #FAILED
        
            self.connection.close()

    def get(self):
        self.cursor.execute("DESCRIBE arms")
        for x in self.cursor:
            print(x)

    def get_table(self, tableName):
        try:
            self.cursor.execute("SELECT * from %s" % (tableName))
            columns = self.cursor.description
            ret = [{columns[index][0]:column for index, column in enumerate(value)} for value in self.cursor.fetchall()]
            #print("Total number of rows in table: ", self.cursor.rowcount)
            #print()
            #print([i[0]for i in self.cursor.description]) # get column names
            return ret
        except:
            print("Error with get_table")
            return -1

    def get_tables(self):
        ret = [] # formatted list

        ######### DIRTIER BUT WORKS
        '''
        tables = []
        tablesCols = []

        # retrive all tables
        self.cursor.execute("SHOW TABLES")
        result = self.cursor.fetchall()
        #sql = "SHOW Columns FROM %s"
        for table in result:
            #val = table # name of table
            #print( type( val ), val)
            #self.cursor.execute( sql, val ) # retrive cols of table(val)
            #data[table] = [column[0] for column in self.cursor.fetchall()]
            tables.append( table[0] )

        
        # retrive all col from said table * SAME AS BELOW
        self.cursor.execute("SHOW Columns FROM arms")
        armCol = [column[0] for column in self.cursor.fetchall()]

        # retrive all col from said table * SAME AS ABOVE
        self.cursor.execute("DESCRIBE push_exercises")
        pushCol = [column[0] for column in self.cursor.fetchall()]

        # retrive all col from said table
        #self.cursor.execute("DESCRIBE test")
        sql = "DESCRIBE %s" % (('test'))
        #val = (('test')) DOESN'T WORK
        #self.cursor.execute(sql) DOESN'T WORK
        #print(val, type(val), type(val[0]))
        self.cursor.execute(sql)
        testCol = [column[0] for column in self.cursor.fetchall()]

        # aggregate all table Columns
        tablesCols = [armCol, pushCol, testCol]

        # formating ret
        for idx in range(len(tables)):
            ret.append( (tables[idx], tablesCols[idx] ) )
        '''
        ######### CLEANER (BU% BROKEN)
        data = {}

        # retrive all tables
        try:
            self.cursor.execute("SHOW TABLES")
            result = self.cursor.fetchall()
            for table in result:
                val = (table[0]) # name of table
                #print( val, type( val ), type(val[0]))
                sql = "DESCRIBE %s" % ((table[0]))
                #print(sql)
                self.cursor.execute( sql ) # retrive cols of table(val)
                data[val] = [column[0] for column in self.cursor.fetchall()]
                print(data)
            return data # RETURN DICTIONARY {Key: name of table, VaL: list of cols}
        except:
            print("WRONGGGGGG")
            return 0

        