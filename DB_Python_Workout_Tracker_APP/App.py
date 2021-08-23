import tkinter as tk
import os
#import Db_translator # imports file so u have to prefix it with name to access methods and such 
from Db_translator import * #imports everything


'''
Variables
'''
pageDict = {}
startPage = basePage = armPage = []
CANVAS_HEIGHT = CANVAS_WIDTH = 500

'''
Definitions
'''

def create_window():
    window = Toplevel(app)

def clicked():
    print(canvas.find_all())

def enter():
    for obj in canvas.find_all():
        canvas.itemconfigure(obj, state='hidden')

'''
app = Tk()
app.title("Workout Companion")
canvas = Canvas(app, width=CANVAS_WIDTH, height=CANVAS_HEIGHT, background = 'AntiqueWhite3')
canvas.grid(row=0)
canvas.create_text(CANVAS_WIDTH/2, CANVAS_HEIGHT/4, text= "Welcome to the Workout Tracker!")
Button(app, text="Enter", command= enter).grid(row=0)
'''

class Application(tk.Frame):
    def __init__(self, master=None):
        super().__init__(master) # RESEARCH
        self.master = master
        self.master.title("Exercise Tracker")
        self.master.config(bg="Blue")
        self.CW = 500
        self.CH = 500
        #self.master.geometry(str(self.CW)+"x"+str(self.CH))
        #self.grid()
        #self.canvas = tk.Canvas(self, width=self.CW/2, height=self.CH/2, background = 'AntiqueWhite3')
        #self.canvas.grid(row=0)
        self.cache = {} # Dictionary to store pages generated
        self.page = "Start Page" # tells which page Currently on (Start Page, Tables Page, arm, etc.)

        # Dedicated back button
        # only active if not on login  page (EITHER DISABLE OR PASS IF self.page = 'Start Page')
        self.back = None
        self.prev = [] #prev list
        self.create_widgets()

        self.db = Translator()
        self.table_entries = None
        self.colNames = [] ###### !!!! 

    def create_widgets(self):
        '''
        self.hi_there = tk.Button(self)
        self.hi_there["text"] = "Hello World\n(click me)"
        self.hi_there["command"] = self.say_hi
        self.hi_there.grid(row=0)
        '''
        self.columnconfigure(0, weight=1) #https://www.pythontutorial.net/tkinter/tkinter-grid/
        self.columnconfigure(1, weight=3) # weight is basically the length

        # username
        self.username_label = tk.Label(self.master, text="Username:")
        self.username_label.grid(column=0, row=0, sticky=tk.W, padx=5, pady=5)

        self.username_entry = tk.Entry(self.master)
        self.username_entry.grid(column=1, row=0, sticky=tk.EW, padx=5, pady=5)   

        # password
        self.password_label = tk.Label(self.master, text="Password:")
        self.password_label.grid(column=0, row=1, sticky=tk.W, padx=5, pady=5)

        self.password_entry = tk.Entry(self.master,  show="*")
        self.password_entry.grid(column=1, row=1, sticky=tk.EW, padx=5, pady=5)

        # login button
        self.login_button = tk.Button(self.master, text="Login", command=self.connect)
        self.login_button.grid(column=1, row=3, sticky=tk.E, padx=5, pady=5)

        # add login to cache:
        self.cache[self.page] = self.master.grid_slaves()

        # back 
        self.back = tk.Button(self.master, text="Back", command=self.back_page, state= "disable")
        self.back.grid(column=3, row=4, pady=5)

        '''
        self.quit = tk.Button(self, text="QUIT", fg="red",
                              command=self.master.destroy)
        self.quit.grid(row=1)
        '''

    ### DB call methods
    def connect(self):
        #os.system('clear')
        if self.db.connect("localhost", self.username_entry.get(), self.password_entry.get(), "exercise_log") == 0:
            #print some error on GUI
            print("ERROR")
        else:
            #call switch pages
            self.switch_page("Tables Page")

    def open_table_view(self, tableName, lstBox):
        print()

        self.colNames = []
        idxs = lstBox.curselection()
        print("selected: ", lstBox.curselection())
        for idx in idxs:
            print(lstBox.get(idx))
            self.colNames.append(lstBox.get(idx))

        print()
        self.table_entries = self.db.get_table(tableName)
        if self.table_entries != -1:
            print(self.table_entries)
            #for entrie in table_entries:

            # cache page
            self.switch_page(tableName)

    def back_page(self):
        print('prevList: ', self.prev)
        # page to go back to
        prev = self.prev.pop((len(self.prev) - 1))
        print('prevList: ', self.prev)

        # disable back button if list empty
        if len(self.prev) == 0:
            self.back["state"] = "disable"
            #self.back.configure()

        # disappear
        for obj in self.master.grid_slaves():
            if obj != self.back:
                obj.grid_remove()

        # re appear
        for obj in self.cache[prev]:
            print("re show: ",obj)
            obj.grid()

        self.page = prev
        print()

        # self.page is Tables Page
        if self.page == "Tables Page":
            self.table_entries = None
        
    ### GUI altercation methods
    def switch_page(self, page=None):

        # if button is disabled, enable it
        if self.back["state"] == "disabled":
            self.back["state"] = "normal"

        # set prev page 
        self.prev.append( self.page )
        print('prev pages: ' + str(self.prev))

        # potentially cache page
        if self.page not in self.cache:

            # Save current page to cache
            self.cache[self.page] = self.master.grid_slaves() # list of objects on current page. (or obj in canvas.find_all():) 

        # remove old page
        print("remove", self.page)
        for obj in self.cache[self.page]:

            #print(obj)

            if obj != self.back:
                # forget - tosses placment of widgets, remove - tracks placment of widgets
                obj.grid_remove()

        # if page in cache then re appear, else instatiate it 
        if page in self.cache: #repopulate page
            print("re appear", page)
            for obj in self.cache[page]:
                obj.grid()

        else:  
            print("instiating page...")
            self.instatiate_page(page)
        
        # update old page to new page
        self.page = page
        #print("current page: " + self.page)

        
    def reaper(self): 
        print("Poof")
        for obj in self.cache[self.page]:
            obj.grid() 

    def instatiate_page(self, page):
        # Table View Page (EX: arms, push_exercises, etc)
        if self.table_entries != None:
            print()

            #keys = ['name', 'reps', 'lbs']
            keys = self.colNames

            for col, key in enumerate(keys):
                # label = col name
                name = tk.Label(self.master, text=key)
                name.grid(column = col, row = 0, padx = 10, pady = 10)

            # row 0 is reserved for names of table
            trueRow = 1

            # dictionary is a dictionary of all columns, self.table_entries is a list of dictonaries of rows
            for row, dictionary in enumerate(self.table_entries):
                
                    #print(dictionary[key])
                    # row 0 2 4
                    # row 1 3 5
                    
                    ############## NOTE !!!! ##############
                    ### THIS WILL BREAK DUE TO THE FACT THAT KEY MIGHT NOT BE IN DICTIONARY
                    for idx in range(len(keys)): 
                        # label = col val
                        value = tk.Label(self.master, text=dictionary[keys[idx]])
                        value.grid(column = idx, row= trueRow + row, padx= 2, pady= 2)

        # Tables Page
        if page == "Tables Page":
            #os.system('clear')
            tables = self.db.get_tables()
            
            if tables != 0:

                self.rowconfigure(0, weight=1) #https://www.pythontutorial.net/tkinter/tkinter-grid/

                for idx, keyVal in enumerate(tables.items()):

                    tableName = keyVal[0]
                    columnNames = keyVal[1]

                    #Make room for table's columns
                    self.rowconfigure(idx + 1, weight=5) # weight is basically the length (ROW = VERT, COL = HORZ)
                    
                    #create table label
                    self.table_label = tk.Label(self.master, text=tableName)
                    self.table_label.grid(column=idx, row=0, padx=5, pady=2)

                    #create table's columns
                    list_items = tk.StringVar(value=columnNames)
                    self.list_box = tk.Listbox(self.master, listvariable=list_items, selectmode='multiple')
                    self.list_box.grid(column=idx, row= 1)

                    #create button to enter table
                    # lambda is a way to pass var to command functions
                    self.button = tk.Button(self.master, text="View", command= lambda t=tableName, selected = self.list_box: self.open_table_view(t, selected))
                    self.button.grid(column=idx, row= 2)


                    

        elif page == "arms Page":
            pass

root = tk.Tk()
app = Application(master=root)
app.mainloop()
