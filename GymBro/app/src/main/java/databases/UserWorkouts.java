package databases;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import org.bson.types.ObjectId;

public class UserWorkouts extends RealmObject {
    @PrimaryKey
    @Required
    private ObjectId _id;
    @Required
    private String Email;

    @Required
    private RealmList<String> Exercises;

    @Required
    private RealmList<String> MuscleList;

    private String Muscle_Group;

    private String Name;


    // Standard getters & setters
    public ObjectId getId() { return _id; }
    public void setId(ObjectId _id) { this._id = _id; }

    public String getEmail() { return Email; }
    public void setEmail(String Email) { this.Email = Email; }

    public RealmList<String> getExercises() { return Exercises; }
    public void setExercises(RealmList<String> Exercises) { this.Exercises = Exercises; }

    public RealmList<String> getMuscleList() { return MuscleList; }
    public void setMuscleList(RealmList<String> MuscleList) { this.MuscleList = MuscleList; }

    public String getMuscleGroup() { return Muscle_Group; }
    public void setMuscleGroup(String Muscle_Group) { this.Muscle_Group = Muscle_Group; }

    public String getName() { return Name; }
    public void setName(String Name) { this.Name = Name; }

}
