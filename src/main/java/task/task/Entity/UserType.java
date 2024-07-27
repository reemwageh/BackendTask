package task.task.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserType")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="type_id")
    private int typeId;

    @Column(name="type_name")
    private String typeName;

    public UserType(int typeId) {
        this.typeId = typeId;
    }


}
