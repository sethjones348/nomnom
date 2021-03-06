package edu.iastate.shoppinglist;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ShoppingListViewModel extends ViewModel implements Serializable {

    public transient MutableLiveData<ShoppingListModel> shoppingListModel = new MutableLiveData<>();

    public ShoppingListViewModel() {
        shoppingListModel.setValue(new ShoppingListModel());
    }

    public void addList(String name){
        ShoppingListModel model = shoppingListModel.getValue();
        model.addList(name);

        shoppingListModel.setValue(model);
    }

    public void addList(ShoppingList duplicatedList){
        ShoppingListModel model = shoppingListModel.getValue();
        model.addList(duplicatedList);

        shoppingListModel.setValue(model);
    }

    public void deleteList(ShoppingList deletedList){
        ShoppingListModel model = shoppingListModel.getValue();
        model.deleteList(deletedList);

        shoppingListModel.setValue(model);
    }

    public ShoppingListModel load(MainActivity mainAct) throws IOException, ClassNotFoundException{
        FileInputStream fis;
        ObjectInputStream ois;


        try {
            fis = new FileInputStream(mainAct.getApplicationContext().getFileStreamPath("shoppingList"));
            ois = new ObjectInputStream(fis);

            shoppingListModel.setValue((ShoppingListModel) ois.readObject());
            ois.close();
            fis.close();
        } catch(Exception e) {
            Log.d("Model", "Exception on loading", e);
        }

//        File f = new File(mainAct.getApplicationContext().getFilesDir() + "/shoppingList");
//        FileInputStream file = new FileInputStream(f);
//        ObjectInputStream output = new ObjectInputStream(file);
//        ShoppingListModel model = (ShoppingListModel)output.readObject();
//
//        output.close();
//        file.close();

//        return model;
    }

    public void write(MainActivity mainAct) throws IOException, ClassNotFoundException{
        File f = new File(mainAct.getApplicationContext().getFilesDir() +"/shoppingList");
        FileOutputStream file = new FileOutputStream(f);
        ObjectOutput output = new ObjectOutputStream(file);
        output.writeObject(this);

        output.close();
        file.close();
    }

    public void saveGameState(Context context, String filename) {
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(mStatesSeen);
            oos.close();
            fos.close();
            Log.d("Model", "Changes written");
        } catch(Exception e) {
            Log.d("Model", "Exception on saving", e);
        }
    }

    public void loadFromFile(Context context, String filename) {
        FileInputStream fos;
        ObjectInputStream oos;

        try {
            fos = new FileInputStream(context.getFileStreamPath(filename));
            oos = new ObjectInputStream(fos);
            mStatesSeen = (HashMap<String, Boolean>) oos.readObject();
            statesSeen.setValue(mStatesSeen);
            oos.close();
            fos.close();
        } catch(Exception e) {
            Log.d("Model", "Exception on loading", e);
        }
    }




}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               