package wfiis.pizzerialesna.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckConnectionToFirebase {

    DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");

    private boolean isConnectd;

    public boolean isConnected(){
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                isConnectd = snapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
        return isConnectd;
    }

}
