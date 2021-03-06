package plus.justice.dispatcher.models.database;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_submission")
public class Submission {
    public final static int LANGUAGE_C = 0;
    public final static int LANGUAGE_CPP = 1;
    public final static int LANGUAGE_JAVA = 2;

    public final static int STATUS_QUEUE = -1;
    public final static int STATUS_AC = 0;
    public final static int STATUS_CE = 1;
    public final static int STATUS_RE = 2;
    public final static int STATUS_TLE = 3;
    public final static int STATUS_MLE = 4;
    public final static int STATUS_WA = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "problem_id")
    private Long problemId;

    @Column(name = "language")
    private Integer language;

    @Column(name = "code")
    private String code;

    @Column(name = "status")
    private Integer status;

    @Column(name = "runtime")
    private Long runtime;

    @Column(name = "memory")
    private Long memory;

    @Column(name = "error")
    private String error;

    @Column(name = "input")
    private String input;

    @Column(name = "output")
    private String output;

    @Column(name = "expected")
    private String expected;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}