package testing1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

public class ReadingProgressTest {
    UserService userService;
    BookService bookService;
    EmailService emailService;

    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);
        bookService = Mockito.mock(BookService.class);
        emailService = Mockito.mock(EmailService.class);
    }

    @Test
    void testUnauthorizedDoesNotSendMail() {
        Mockito.doReturn(false).when(userService).isMarketingAuthorised(0);

        var readingProcess = new ReadingProgress(userService, bookService, emailService);
        readingProcess.readingProgress(0, 0, 0);

        Mockito.verify(emailService, Mockito.never()).sendKeepUpGoodWorkEmail(0);
        Mockito.verify(emailService, Mockito.never()).sendAlmostThereEmail(0);
        Mockito.verify(emailService, Mockito.never()).sendCongratsYouHaveMadeItEmail(0);
    }

    @Test
    void testSendKeepUpMailBelow50() {
        Mockito.doReturn(true).when(userService).isMarketingAuthorised(0);

        var readingProcess = new ReadingProgress(userService, bookService, emailService);
        readingProcess.readingProgress(0, 0, 49.99);

        Mockito.verify(emailService, Mockito.times(1)).sendKeepUpGoodWorkEmail(0);
        Mockito.verify(emailService, Mockito.never()).sendAlmostThereEmail(0);
        Mockito.verify(emailService, Mockito.never()).sendCongratsYouHaveMadeItEmail(0);
    }

    @ParameterizedTest
    @ValueSource(doubles = { 50.0, 98.99 })
    void testSendAlmostThereMailBetween50and99(double progress) {
        Mockito.doReturn(true).when(userService).isMarketingAuthorised(0);

        var readingProcess = new ReadingProgress(userService, bookService, emailService);
        readingProcess.readingProgress(0, 0, progress);

        Mockito.verify(emailService, Mockito.never()).sendKeepUpGoodWorkEmail(0);
        Mockito.verify(emailService, Mockito.times(1)).sendAlmostThereEmail(0);
        Mockito.verify(emailService, Mockito.never()).sendCongratsYouHaveMadeItEmail(0);
    }

    @Test
    void testSendYouHaveMadeItMailAbove99() {
        Mockito.doReturn(true).when(userService).isMarketingAuthorised(0);

        var readingProcess = new ReadingProgress(userService, bookService, emailService);
        readingProcess.readingProgress(0, 0, 99.0);

        Mockito.verify(emailService, Mockito.never()).sendKeepUpGoodWorkEmail(0);
        Mockito.verify(emailService, Mockito.never()).sendAlmostThereEmail(0);
        Mockito.verify(emailService, Mockito.times(1)).sendCongratsYouHaveMadeItEmail(0);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void testProgressUpdate(boolean access) {
        Mockito.doReturn(access).when(userService).isMarketingAuthorised(0);

        var readingProcess = new ReadingProgress(userService, bookService, emailService);
        readingProcess.readingProgress(0, 0, 0);

        Mockito.verify(bookService, Mockito.times(1)).updateLastVisualization(0, 0);
    }
}
