package com.example.libraryadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Book;
import com.example.model.EditDeleteBookAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TabHost tabHost;

    // Hashmap consisting bookISBN
    HashMap<String,Boolean>mapKey;
    //AddBookTab:
    TextView txtISBN_AddBookTab, txtBookName_AddBookTab,txtPublishedYear_AddBookTab,txtQuantity_AddBookTab;
    AutoCompleteTextView autotxtAuthor_AddBookTab,autotxtGenre_AddBookTab;
    Button btnAddBook_AddBookTab;
    ArrayList<String>arrAuthor_AddBookTab;
    ArrayList<String>arrGenre_AddBookTab;
    ArrayAdapter<String>adapterAuthor_AddBookTab;
    ArrayAdapter<String>adapterGenre_AddBookTab;
    DatabaseReference bookReference;

    //EditDeleteBookTab:
    TextView txtISBN_EditDeleteBook, txtBookName_EditDeleteBook, txtPublishedYear_EditDeleteBook, txtQuantity_EditDeleteBook;
    Button btnUpdate_EditDeleteBook, btnDelete_EditDeleteBook;
    ImageButton btnFind_EditDeleteBook;

    // autoGenre va autoAuthor se dung chung adapter tuong ung voi addbooktab
    AutoCompleteTextView autoTxtSearch_EditDeleteBook, autoTxtAuthor_EditDeleteBook, autoTxtGenre_EditDeleteBook;
    ArrayAdapter<String>adapterSearch_EditDeleteBook;
    ArrayList<String>arrSearch_EditDeleteBook;
    HashMap<String,Book>mapBook_EditDeleteTab;

    ListView lvBookList_EditDeleteBook;
    ArrayList<Book>arrBook_EditDeleteBook;
    EditDeleteBookAdapter adapterEditDelete_EditDeleteBook;

    int selectedItemPosition = -1;
    Book updatedBook;

    //Genereal Edit Tab:
    TextView txtQuantity_GeneralEdit;
    Button btnUpdateQuantity_GeneralEdit, btnChangePassword_GeneralEdit, btnSignOut_GeneralEdit;
    DatabaseReference maximumBookQuantityReference;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        addTab1Events();
        addTab2Events();
        addTab3Events();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            long l = Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    private void addTab3Events() {
        btnUpdateQuantity_GeneralEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNumeric(txtQuantity_GeneralEdit.getText().toString()))
                maximumBookQuantityReference.setValue(Long.parseLong(txtQuantity_GeneralEdit.getText().toString()));
                long c=Long.parseLong(txtQuantity_GeneralEdit.getText().toString());
                Toast.makeText(MainActivity.this,"Updated Successfully!",Toast.LENGTH_SHORT).show();
                txtQuantity_GeneralEdit.setText(String.valueOf(c));
            }
        });
        btnSignOut_GeneralEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btnChangePassword_GeneralEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    private void addTab2Events() {
btnFind_EditDeleteBook.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
ArrayList<Book> foundBooks=findBook(autoTxtSearch_EditDeleteBook.getText().toString());
arrBook_EditDeleteBook.clear();
for(Book book:foundBooks){
    arrBook_EditDeleteBook.add(book);
}
adapterEditDelete_EditDeleteBook.notifyDataSetChanged();
    }
});

btnUpdate_EditDeleteBook.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(Integer.parseInt(txtPublishedYear_EditDeleteBook.getText().toString())<1900||
                Integer.parseInt(txtQuantity_EditDeleteBook.getText().toString())<0){
            Toast.makeText(MainActivity.this,"Quantity or Published year is invalid! Please try again",Toast.LENGTH_SHORT).show();
        }else{
            updatedBook=new Book(txtISBN_EditDeleteBook.getText().toString(),
                    autoTxtAuthor_EditDeleteBook.getText().toString(),
                    txtBookName_EditDeleteBook.getText().toString(),
                    autoTxtGenre_EditDeleteBook.getText().toString(),
                    Integer.parseInt(txtPublishedYear_EditDeleteBook.getText().toString()),
                    Integer.parseInt(txtQuantity_EditDeleteBook.getText().toString()));
            bookReference.child(txtISBN_EditDeleteBook.getText().toString()).setValue(updatedBook);
            Toast.makeText(MainActivity.this,"Book updated successfully!",Toast.LENGTH_SHORT).show();

            txtISBN_EditDeleteBook.setText("");
            txtPublishedYear_EditDeleteBook.setText("");
            txtQuantity_EditDeleteBook.setText("");
            txtBookName_EditDeleteBook.setText("");
            autoTxtAuthor_EditDeleteBook.setText("");
            autoTxtGenre_EditDeleteBook.setText("");
        }
    }
});

btnDelete_EditDeleteBook.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
bookReference.child(txtISBN_EditDeleteBook.getText().toString()).removeValue();
Toast.makeText(MainActivity.this,"Book deleted successfully!",Toast.LENGTH_SHORT).show();
txtISBN_EditDeleteBook.setText("");
txtPublishedYear_EditDeleteBook.setText("");
txtQuantity_EditDeleteBook.setText("");
txtBookName_EditDeleteBook.setText("");
autoTxtAuthor_EditDeleteBook.setText("");
autoTxtGenre_EditDeleteBook.setText("");
    }
});

lvBookList_EditDeleteBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position>=0){
            Book book=arrBook_EditDeleteBook.get(position);
            txtISBN_EditDeleteBook.setText(book.getISBN());
            txtBookName_EditDeleteBook.setText(book.getBookName());
            autoTxtAuthor_EditDeleteBook.setText(book.getAuthor());
            autoTxtGenre_EditDeleteBook.setText(book.getGenre());
            txtPublishedYear_EditDeleteBook.setText(String.valueOf(book.getPublishedYear()));
            txtQuantity_EditDeleteBook.setText(String.valueOf(book.getQuantity()));
            selectedItemPosition=position;
        }
    }
});
    }
    private ArrayList<Book> findBook(String toString) {
        ArrayList<Book>arrFoundBooks=new ArrayList<>();
        for(String s:mapBook_EditDeleteTab.keySet()){
            if(s.contains(toString)){
                arrFoundBooks.add(mapBook_EditDeleteTab.get(s));
            }
        }
        return arrFoundBooks;
    }
    public boolean checkISBNTotallyNew(){
        if (mapKey.get(txtISBN_AddBookTab.getText().toString())==null)
            return true;
        return false;
    }
    private void addTab1Events() {
        btnAddBook_AddBookTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkISBNTotallyNew()==true){
                    Book book=new Book(txtISBN_AddBookTab.getText().toString(),
                            autotxtAuthor_AddBookTab.getText().toString(),
                            txtBookName_AddBookTab.getText().toString(),
                            autotxtGenre_AddBookTab.getText().toString(),
                            Integer.parseInt(txtPublishedYear_AddBookTab.getText().toString()),
                            Integer.parseInt(txtQuantity_AddBookTab.getText().toString()));
                   bookReference.child(txtISBN_AddBookTab.getText().toString()).setValue(book);
                    Toast.makeText(MainActivity.this,"Book added successfully!",Toast.LENGTH_SHORT).show();

                    txtISBN_AddBookTab.setText("");
                    txtPublishedYear_AddBookTab.setText("");
                    txtQuantity_AddBookTab.setText("");
                    txtBookName_AddBookTab.setText("");
                    autotxtAuthor_AddBookTab.setText("");
                    autotxtGenre_AddBookTab.setText("");
                }else{
                    Toast.makeText(MainActivity.this,"Book already existed! Please try again!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void addControls() {
        addTabHosts();
        addAddBookTabControls();
        addEditDeleteTabControls();
        addGeneralEditTabControls();
        uploadInformation();
    }
    private void addGeneralEditTabControls() {
        maximumBookQuantityReference = FirebaseDatabase.getInstance().getReference().child("root").child("MaximumBookQuantity");
        txtQuantity_GeneralEdit=findViewById(R.id.txtQuantity_GeneralEdit);
        btnUpdateQuantity_GeneralEdit=findViewById(R.id.btnUpdateQuantity_GeneralEdit);
        btnChangePassword_GeneralEdit=findViewById(R.id.btnChangePassword_GeneralEdit);
        btnSignOut_GeneralEdit=findViewById(R.id.btnSignOut_GeneralEdit);
    }
    private void addEditDeleteTabControls() {
txtISBN_EditDeleteBook=findViewById(R.id.txtISBN_EditDeleteBook);
txtBookName_EditDeleteBook=findViewById(R.id.txtBookName_EditDeleteBook);
txtPublishedYear_EditDeleteBook=findViewById(R.id.txtPublishedYear_EditDeleteBook);
txtQuantity_EditDeleteBook=findViewById(R.id.txtQuantity_EditDeleteBook);

btnFind_EditDeleteBook=findViewById(R.id.btnFind_EditDeleteBook);
btnUpdate_EditDeleteBook=findViewById(R.id.btnUpdate_EditDeleteBook);
btnDelete_EditDeleteBook=findViewById(R.id.btnDelete_EditDeleteBook);

autoTxtAuthor_EditDeleteBook=findViewById(R.id.autoTxtAuthor_EditDeleteBook);
autoTxtGenre_EditDeleteBook=findViewById(R.id.autoTxtGenre_EditDeleteBook);
autoTxtSearch_EditDeleteBook=findViewById(R.id.autoTxtSearch_EditDeleteBook);

autoTxtAuthor_EditDeleteBook.setAdapter(adapterAuthor_AddBookTab);
autoTxtGenre_EditDeleteBook.setAdapter(adapterGenre_AddBookTab);

arrSearch_EditDeleteBook=new ArrayList<>();
adapterSearch_EditDeleteBook=new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,arrSearch_EditDeleteBook);
autoTxtSearch_EditDeleteBook.setAdapter(adapterSearch_EditDeleteBook);

arrBook_EditDeleteBook=new ArrayList<>();
adapterEditDelete_EditDeleteBook=new EditDeleteBookAdapter(MainActivity.this,R.layout.lvbookitem,arrBook_EditDeleteBook);
lvBookList_EditDeleteBook=findViewById(R.id.lvBookList_EditDeleteBook);
lvBookList_EditDeleteBook.setAdapter(adapterEditDelete_EditDeleteBook);

mapBook_EditDeleteTab=new HashMap<>();
    }
    private void addAddBookTabControls() {
        bookReference = FirebaseDatabase.getInstance().getReference().child("root").child("Book");
        txtISBN_AddBookTab=findViewById(R.id.txtISBN_AddBookTab);
        txtBookName_AddBookTab=findViewById(R.id.txtBookName_AddBookTab);
        txtPublishedYear_AddBookTab=findViewById(R.id.txtPublishedYear_AddBookTab);
        txtQuantity_AddBookTab=findViewById(R.id.txtQuantity_AddBookTab);

        mapKey=new HashMap<String, Boolean>();

        autotxtAuthor_AddBookTab=findViewById(R.id.autotxtAuthor_AddBookTab);
        autotxtGenre_AddBookTab=findViewById(R.id.autotxtGenre_AddBookTab);

        btnAddBook_AddBookTab=findViewById(R.id.btnAddBook_AddBookTab);

        arrAuthor_AddBookTab=new ArrayList<>();
        arrGenre_AddBookTab=new ArrayList<>();

        adapterAuthor_AddBookTab=new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,arrAuthor_AddBookTab);
        adapterGenre_AddBookTab=new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,arrGenre_AddBookTab);

        autotxtAuthor_AddBookTab.setAdapter(adapterAuthor_AddBookTab);
        autotxtGenre_AddBookTab.setAdapter(adapterGenre_AddBookTab);

    }
    private void uploadInformation() {

bookReference.addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Book book=dataSnapshot.getValue(Book.class);
            arrBook_EditDeleteBook.add(book);
            arrAuthor_AddBookTab.add(book.getAuthor());
            arrGenre_AddBookTab.add(book.getGenre());

            arrSearch_EditDeleteBook.add(book.getBookName());
            arrSearch_EditDeleteBook.add(book.getISBN());

            adapterGenre_AddBookTab.notifyDataSetChanged();
            adapterAuthor_AddBookTab.notifyDataSetChanged();
            adapterEditDelete_EditDeleteBook.notifyDataSetChanged();
            adapterSearch_EditDeleteBook.notifyDataSetChanged();

            mapKey.put(book.getISBN(),true);
           String bookToString = book.getISBN()+", "+book.getBookName();
           mapBook_EditDeleteTab.put(bookToString,book);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
arrBook_EditDeleteBook.set(selectedItemPosition,updatedBook);
adapterEditDelete_EditDeleteBook.notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
arrBook_EditDeleteBook.remove(selectedItemPosition);
adapterEditDelete_EditDeleteBook.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

maximumBookQuantityReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Long maximumBookQuantity=dataSnapshot.getValue(Long.class);
        txtQuantity_GeneralEdit.setText(String.valueOf(maximumBookQuantity));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }
    private void addTabHosts() {
        tabHost=findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1=tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("ADD BOOK");
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2=tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("EDIT/DELETE BOOK");
        tabHost.addTab(tab2);

        TabHost.TabSpec tab3=tabHost.newTabSpec("t3");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator("GENERAL");
        tabHost.addTab(tab3);
    }
}
