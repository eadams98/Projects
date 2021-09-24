import tkinter as tk
import os
#import Db_translator # imports file so u have to prefix it with name to access methods and such 
from Db_translator import * #imports everything

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
        self.view = None # only for pages with multiple pages worth of information (Ex: tables view)
        self.view_page_widget_placment_dictionary = None # maybe call it cache instead

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

        #if going back from a page with multiple turning pages reset it to self.view to None
        if self.view != None:
            self.view = None
            self.view_page_widget_placment_dictionary = None

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

###########################################

    def next_view(self, views, placement, nextBtn, backBtn): # views: list of list of tups, placment: dict w/ tup keys and list of tup values, currentView: int
        self.view += 1
        trueRow = 1

        # if at last view disable next button
        if self.view == len(views) - 1:
            # disable next view button 
            nextBtn["state"] = "disabled"

        # if back button disabled enable it
        if backBtn["state"] == "disabled":
            backBtn["state"] = "normal"

        print(self.view, nextBtn["state"], len(views), backBtn["state"])

        # remove all old elements of old page, except for back, next view and back view buttons
        for obj in self.master.grid_slaves():
            if obj not in (self.back, nextBtn, backBtn):
                obj.grid_remove()

        # redundant code. clean up by making it into a function
        if self.view in self.view_page_widget_placment_dictionary:

            for obj in self.view_page_widget_placment_dictionary[self.view]:
                obj.grid()
        else:

            #print("OGOOGAABOOGA:",  views, len(views[ self.view ][ views[self.view].index("STOP") + 1]) )
            #print(data[self.view].index("STOP"))

            '''
            GAVE PROBLEMS (INDEX ERROR) IF DATA WAS EMPTY
            if len(views[ self.view ][ views[self.view].index("STOP") + 1]) > 0:
            '''
            for idx in range(0, views[self.view].index("STOP")): # top columns
                        print(views[self.view][idx])
                        name = tk.Label(self.master, text=views[self.view][idx]) # all these will be tups with singular values
                        place = placement[views[self.view][idx]]
                        name.grid(column = place['column'], row = place['row'], padx = place['padx'], pady = place['pady'])

            print('placement: ', placement)
            for idx in range(views[self.view].index("STOP") + 1, len(views[self.view]) ): #row data (in tuple form)
                
                if views[self.view][idx] != "STOP": # CAUSE A LINE BREAK BETWEEN COLS AND ROWS
                    for idx2, val in enumerate(views[self.view][idx]): # val = tuple(), idx2 = used for col indicator
                        
                        value = tk.Label(self.master, text=str(val))
                        place = placement[views[self.view][idx2]]
                        print('Val: ', val, 'place:', place) #place is correct up here but gets overwritten below
                        
                        if len(place) > 1:
                            data = views[self.view]
                            data = data[data.index("STOP") : idx]
                            print("data: ",data, views[self.view][idx], placement[views[self.view][idx]], data.count(views[self.view][idx]))
                            place = placement[ views[self.view][idx] ][ data.count(views[self.view][idx]) ]
                            place = place[idx2] # !!!!!!!! FIX THIS: idk why returned item is contained/wrapped in another list
                        print("place: ", place)
                        value.grid(column = place['column'], row= place['row'], padx= place['padx'], pady= place['pady'])

            # cache page regardless of number of columns
            self.view_page_widget_placment_dictionary[self.view] = [obj for obj in self.master.grid_slaves() if obj not in (self.back, nextBtn, backBtn)]
        

    def back_view(self, views, placement, backBtn, nextBtn):
        self.view -= 1
        if self.view == 0: 
            # disable back view button 
            backBtn["state"] = "disabled"

        if nextBtn["state"] == "disabled":
            nextBtn["state"] = "normal"

        # remove all old elements of old page, except for back, next view and back view buttons
        for obj in self.master.grid_slaves():
            if obj not in (self.back, nextBtn, backBtn):
                obj.grid_remove()

        if self.view in self.view_page_widget_placment_dictionary:
            for obj in self.view_page_widget_placment_dictionary[self.view]:
                obj.grid()

        

#############################################
        
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

            if len(keys) < 4: #prevent table elements from overwriting the back button 
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

            else: # incase of there being 4 or more rows make multiple views of said page with a max of 3 elems a page
                
                # list indicies indicate which page you're on (EX: views[0] = page 1's keys)

                if len(keys) % 3 == 0:
                    views = [ [] for x in range( ((len(keys) // 3)) ) ] # make num of list needed for as many pages needed (each list contains keys for placement)
                else:
                    views = [ [] for x in range( ((len(keys) // 3) + 1) ) ] # make num of list needed for as many pages needed (each list contains keys for placement)
                
                placement = {} # create dictionary (key: name of col, value: nums responsible for placement in grid)
                
                for col, key in enumerate(keys):
                    # label = col val
                    #name = tk.Label(self.master, text=key)
                    #name.grid(column = col, row = 0, padx = 10, pady = 10)

                    # these are singular string names. I keep them as tuples just to match the rest of the entries in
                    # placement dictionary
                    print(col,len(views))
                    views[col//3].append(tuple([key]))
                    placement[tuple([key])] = {"column" : col % 3, "row" : 0,  "padx" : 10, "pady" : 10}

                # Figure this is a sloppy solution, but its responsible to indicationg where the seperation between 
                # column names and actual data entries (rows)
                for key_list in views:
                    key_list.append("STOP")

                # row 0 is reserved for names of table
                trueRow = 1 

                # dictionary is a dictionary of all columns, self.table_entries is a list of dictonaries of rows
                for row, dictionary in enumerate(self.table_entries):
                    
                        #print(dictionary[key])
                        # row 0 2 4
                        # row 1 3 5
                        
                        ############## NOTE !!!! ##############
                        ### THIS WILL BREAK DUE TO THE FACT THAT KEY MIGHT NOT BE IN DICTIONARY

                        ### FOR THIS YOU HAVE TO HAVE A TUPLE OF all 3 elements as the key and list of 3 dictionaries as value
                        ### every 3 iteration u append tuple and list of dicts you made and reset them for next time
                        tup_key = []

                        for idx in range(len(keys)): 
                            # label = col val
                            
                            #value = tk.Label(self.master, text=dictionary[keys[idx]])
                            #value.grid(column = idx, row= trueRow + row, padx= 2, pady= 2)
                            #print("idx: ",idx)
                        
                            # HAPPENS EVERY 3 iterations
                            ## ISSUE: trouble making keys unique. works well if at least one of the elements inside tup_key
                            ##          are unique, otherwise it will cause overwriting issues
                            ## SOLUTION: allows duplicate keys. values are now List of List of tups. if a key is a dup
                            ##              the value (which is a list of tups) will just be appended to the current value (list of list of tups)
                            ## LOOK UP SPEED: it is entirely dependent on  tup_key (list where keys are stored). at worst O(n) (linear search)
                            ##                  finds out which list of tups to use from value by using the dup keys placment amoung dup keys
                            ##                  EX: if this is the 3rd time this dup has been seen then we us 3 when indexing the values

                            if (idx - 2) % 3 == 0: # offset it by 2 so it happens at (2, 5, 7, etc...)
                                tup_key.append(dictionary[keys[idx]])
                                #print("keys = ", tuple(tup_key))
                                views[idx//3].append( tuple(tup_key) )

                                # value is list of list of tup
                                # REASON: in case of duplicates we just append to list instead of overwriting
                                if tuple(tup_key) not in placement:
                                    placement[tuple(tup_key)] = [
                                                                [
                                        {"column" : (idx - 2) % 3, "row" : trueRow + row, "padx" : 2, "pady" : 2},
                                        {"column" : (idx - 1) % 3, "row" : trueRow + row, "padx" : 2, "pady" : 2},
                                        {"column" : idx % 3, "row" : trueRow + row, "padx" : 2, "pady" : 2}
                                                                ]                            
                                                                ]

                                else:
                                    placement[tuple(tup_key)].append(
                                        [
                                        {"column" : (idx - 2) % 3, "row" : trueRow + row, "padx" : 2, "pady" : 2},
                                        {"column" : (idx - 1) % 3, "row" : trueRow + row, "padx" : 2, "pady" : 2},
                                        {"column" : idx % 3, "row" : trueRow + row, "padx" : 2, "pady" : 2}
                                        ] 
                                        )

                                tup_key = []

                            elif idx == len(keys) - 1: # not a perfect %3 make sure you still take care of it
                                tup_key.append(dictionary[keys[idx]])
                                tmp = [[]] # list of list of tups in case of duplicates
                                views[idx//3].append( tuple(tup_key) )

                                for x in range(len(tup_key)):
                                    
                                    tmp[0].append({"column" : (x) % 3, "row" : trueRow + row, "padx" : 2, "pady" : 2})

                                if tuple(tup_key) not in placement:
                                    placement[tuple(tup_key)] = tmp
                                else:
                                    placement[tuple(tup_key)].append(tmp[0])
                                
                            
                            else:
                                #views[idx//3].append( tuple(tup_key) )
                                tup_key.append( dictionary[keys[idx]] )
                print(placement.items())
                print(len(placement.keys()), len(views))
                print()
                print("views: ", views, len(views))
                self.view = 0 # ONLY SHOULD HAVE A VALUE IF MULTIPLE PAGES TO TURN TO EXIST

                if len(views) > 1:
                    #tk.Button(self.master, text="View", command= lambda t=tableName, selected = self.list_box: self.open_table_view(t, selected))
                    #self.button.grid(column=idx, row= 2)
                    next_view = tk.Button(self.master, text = "next view", command= lambda: self.next_view(views, placement, next_view, back_view))
                    next_view.grid(column = 3, row = 1)
                    back_view = tk.Button(self.master, text = "back view", command= lambda: self.back_view(views, placement, back_view, next_view))
                    back_view.grid(column = 3, row = 2)
                    back_view["state"] = "disabled"

                for idx in range(3): # top columns
                    print(views[0][idx])
                    name = tk.Label(self.master, text=views[0][idx]) # all these will be tups with singular values
                    place = placement[views[0][idx]]
                    name.grid(column = place['column'], row = place['row'], padx = place['padx'], pady = place['pady'])

                for idx in range(3, len(views[0]) ): #row data (in tuple form)
                    
                    
                    if views[0][idx] != "STOP": # CAUSE A LINE BREAK BETWEEN COLS AND ROWS
                        for idx2, val in enumerate(views[0][idx]): # val = tuple()
                            value = tk.Label(self.master, text=str(val))
                            value.grid(column = idx2, row= trueRow + (idx-3), padx= 2, pady= 2)

                self.view_page_widget_placment_dictionary = {}
                self.view_page_widget_placment_dictionary[self.view] = [obj for obj in self.master.grid_slaves() if obj not in (self.back, next_view, back_view)]
                '''
                for obj in self.master.grid_slaves():
                    if obj not in (self.back, nextBtn, backBtn):
                        obj.grid_remove()
                '''



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
