package plus.justice.dispatcher.judgers;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;
import plus.justice.dispatcher.Application;
import plus.justice.dispatcher.models.database.Submission;
import plus.justice.dispatcher.models.sandbox.TaskResult;
import plus.justice.dispatcher.workers.impl.JavaWorker;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaJudgerTest {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JavaWorker javaWorker;

    private Random random = new Random();

    private TaskResult getTaskResult(String s) throws IOException {
        Submission submission = new Submission();
        submission.setId(Math.abs(random.nextLong()));
        submission.setProblemId(1L);
        submission.setLanguage(Submission.LANGUAGE_JAVA);
        submission.setCode(new String(Files.readAllBytes(resourceLoader.getResource(s).getFile().toPath())));
        return javaWorker.work(submission);
    }


    @Test
    public void t000AC() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/0.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_AC);
    }

    @Test
    public void t001PlainText() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/1.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_CE);
        assertThat(taskResult.getError()).contains("class, interface, or enum expected");
    }

    @Test
    public void t002SyntaxError() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/2.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_CE);
        assertThat(taskResult.getError()).contains("error: ';' expected");
    }

    @Test
    public void t003OutOfIndex() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/3.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_RE);
        assertThat(taskResult.getError()).contains("String index out of range").contains("Exception ");
    }

    @Test
    public void t004NullPointerException() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/4.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_RE);
        assertThat(taskResult.getError()).contains("NullPointerException");
    }

    @Test
    public void t005ProhibitReadingFile() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/5.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_RE);
        assertThat(taskResult.getError())
                .contains("java.security.AccessControlException: access denied")
                .contains("java.io.FilePermission");
    }

    @Test
    public void t006ProhibitWritingFile() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/6.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_RE);
        assertThat(taskResult.getError())
                .contains("java.security.AccessControlException: access denied")
                .contains("java.io.FilePermission");
    }

    @Test
    public void t007ProhibitAcceptingSocket() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/7.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_RE);
        assertThat(taskResult.getError())
                .contains("java.security.AccessControlException: access denied")
                .contains("java.net.SocketPermission");
    }

    @Test
    public void t008ProhibitConnectingSocket() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/8.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_RE);
        assertThat(taskResult.getError())
                .contains("java.security.AccessControlException: access denied")
                .contains("java.net.SocketPermission");
    }

    @Test
    public void t009ProhibitCallingCLI() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/9.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_RE);
        assertThat(taskResult.getError())
                .contains("java.security.AccessControlException: access denied")
                .contains("java.io.FilePermission");
    }

    @Test
    public void t010ProhibitGettingEnvParam() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/10.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_RE);
        assertThat(taskResult.getError())
                .contains("java.security.AccessControlException: access denied")
                .contains("java.lang.RuntimePermission");
    }

    @Test
    public void t011CPURuntimeLimitExceeded() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/11.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_TLE);
        assertThat(taskResult.getError()).contains("Time Limit Exceeded");
    }

    @Test
    public void t012RuntimeLimitExceeded() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/12.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_TLE);
        assertThat(taskResult.getError()).contains("Time Limit Exceeded");
    }

    @Test
    public void t013WA() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/13.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_WA);
        assertThat(taskResult.getInput()).contains("07:05:45PM");
        assertThat(taskResult.getOutput()).contains("1234");
        assertThat(taskResult.getExpected()).contains("19:05:45");
    }

    @Test
    public void t014JavaSecurityManagerWhiteList() throws Exception {
        TaskResult taskResult = getTaskResult("classpath:tests/java/14.java");

        assertThat(taskResult.getStatus()).isEqualTo(Submission.STATUS_WA);
        assertThat(taskResult.getOutput()).contains("1.8.0_");
    }
}