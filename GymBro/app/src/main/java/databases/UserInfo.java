package databases;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import org.bson.types.ObjectId;

public class UserInfo extends RealmObject {
    @PrimaryKey
    @Required
    private ObjectId _id;

    @Required
    private String Email;

    private String Height;
    private String Weight;
    private String ActivityLevel;
    private String Goal;
    private String DateOfBirth;
    private String Gender;
    private String Muscle_Group;
    private String Username;


    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getActivityLevel() {
        return ActivityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        ActivityLevel = activityLevel;
    }

    public String getGoal() {
        return Goal;
    }

    public void setGoal(String goal) {
        Goal = goal;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getMuscle_Group() {
        return Muscle_Group;
    }

    public void setMuscle_Group(String muscle_Group) {
        Muscle_Group = muscle_Group;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}

