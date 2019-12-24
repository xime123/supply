package bd.com.walletdb.manager;

import java.util.List;

import bd.com.walletdb.action.ContactAction;
import bd.com.walletdb.entity.ContactEntity;

public class ContactManager {
    private static ContactManager manager = new ContactManager();

    private ContactManager() {
    }

    public static ContactManager getManager() {
        return manager;
    }

    public void insertContact(ContactEntity contactEntity){
        ContactAction action=new ContactAction();
        action.insertOrReplace(contactEntity);
    }

    public List<ContactEntity> getContactList(){
        ContactAction action=new ContactAction();
        return action.loadAll();
    }

    public void delteContact(String address){
        ContactAction action=new ContactAction();
        action.deleteByKey(address);
    }

    public void updateContact(ContactEntity entity){
        ContactAction action=new ContactAction();
        action.update(entity);
    }
}
