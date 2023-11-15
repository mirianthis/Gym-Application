package databases;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import org.bson.types.ObjectId;

public class ExerciseLibrary extends RealmObject implements Parcelable {
    @PrimaryKey
    private ObjectId _id;

    private String Exercise;

    private String Gifs;

    private String Level;

    private String Muscle_Group;

    private String P_P;

    private String U_L_C;

    // Standard getters & setters
    public ObjectId getId() { return _id; }
    public void setId(ObjectId _id) { this._id = _id; }

    public String getExercise() { return Exercise; }
    public void setExercise(String Exercise) { this.Exercise = Exercise; }

    public String getGifs() { return Gifs; }
    public void setGifs(String Gifs) { this.Gifs = Gifs; }

    public String getLevel() { return Level; }
    public void setLevel(String Level) { this.Level = Level; }

    public String getMuscleGroup() { return Muscle_Group; }
    public void setMuscleGroup(String Muscle_Group) { this.Muscle_Group = Muscle_Group; }

    public String getPP() { return P_P; }
    public void setPP(String P_P) { this.P_P = P_P; }

    public String getULC() { return U_L_C; }
    public void setULC(String U_L_C) { this.U_L_C = U_L_C; }

    public int describeContents() {
        return 0;
    }

    public ExerciseLibrary(){}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Exercise);
        dest.writeString(Level);
        dest.writeString(Muscle_Group);
        dest.writeString(U_L_C);
        dest.writeString(Gifs);
    }

    // Add a constructor that reads from a Parcel
    protected ExerciseLibrary(Parcel in) {
        Exercise = in.readString();
        Level = in.readString();
        Muscle_Group = in.readString();
        U_L_C = in.readString();
        Gifs = in.readString();
    }

    // Create a Parcelable.Creator<Data> constant and implement the createFromParcel and newArray methods
    public static final Parcelable.Creator<ExerciseLibrary> CREATOR = new Parcelable.Creator<ExerciseLibrary>() {
        @Override
        public ExerciseLibrary createFromParcel(Parcel in) {
            return new ExerciseLibrary(in);
        }

        @Override
        public ExerciseLibrary[] newArray(int size) {
            return new ExerciseLibrary[size];
        }
    };
}
