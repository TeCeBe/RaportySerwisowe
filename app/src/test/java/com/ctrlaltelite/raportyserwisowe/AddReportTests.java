package com.ctrlaltelite.raportyserwisowe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Map;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28) // Możesz dostosować SDK, na którym testujesz
public class AddReportTests {

    @Mock
    private FirebaseFirestore mockDb;
    @Mock
    private FirebaseAuth mockAuth;
    @Mock
    private FirebaseUser mockUser;
    @Mock
    private DocumentReference mockDocumentReference;

    private AddReport addReport;
    private AddReport activity;

    @Mock
    private EditText mockEditTextDate;
    @Mock
    private EditText mockEditTextTime;
    @Mock
    private EditText mockEditTitle;
    @Mock
    private EditText mockEditContent;
    @Mock
    private EditText mockEditPlace;


    @Before
    public void setUp() {
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn("testUserId");

        addReport = new AddReport();
        addReport.db = mockDb;
        addReport.mAuth = mockAuth;

        addReport.editTitle = mock(EditText.class);
        addReport.editContent = mock(EditText.class);
        addReport.editPlace = mock(EditText.class);
        addReport.editTextDate = mock(EditText.class);
        addReport.editTextTime = mock(EditText.class);

        activity = Mockito.spy(AddReport.class);

        // Initialize the EditText fields with mock objects
        activity.editTextDate = mockEditTextDate;
        activity.editTextTime = mockEditTextTime;
        activity.editTitle = mockEditTitle;
        activity.editContent = mockEditContent;
        activity.editPlace = mockEditPlace;
    }

    @Test
    public void testAddToFirestore() {
        // Mock the getText() method to return a sample value
        Mockito.when(mockEditTextDate.getText()).thenReturn(new EditableString("2023-06-01"));
        Mockito.when(mockEditTextTime.getText()).thenReturn(new EditableString("12:00:00"));
        Mockito.when(mockEditTitle.getText()).thenReturn(new EditableString("Test Title"));
        Mockito.when(mockEditContent.getText()).thenReturn(new EditableString("Test Content"));
        Mockito.when(mockEditPlace.getText()).thenReturn(new EditableString("Test Place"));
        // Prepare data
        when(addReport.editTitle.getText().toString()).thenReturn("Test Title");
        when(addReport.editContent.getText().toString()).thenReturn("Test Content");
        when(addReport.editPlace.getText().toString()).thenReturn("Test Place");
        when(addReport.editTextDate.getText().toString()).thenReturn("2023-01-01");
        when(addReport.editTextTime.getText().toString()).thenReturn("12:00:00");

        // Call the method
        addReport.addToFirestore("Test Title", "Test Content", "2023-01-01", "12:00:00", "Test Place");

        // Capture the data
        ArgumentCaptor<Map<String, Object>> dataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockDb.collection("reports")).add(dataCaptor.capture());

        // Validate the data
        Map<String, Object> capturedData = dataCaptor.getValue();
        assertEquals("Test Title", capturedData.get("title"));
        assertEquals("Test Content", capturedData.get("content"));
        assertEquals("2023-01-01", capturedData.get("date"));
        assertEquals("12:00:00", capturedData.get("time"));
        assertEquals("Test Place", capturedData.get("place"));
        assertEquals("testUserId", capturedData.get("userId"));
    }

    @Test
    public void testSendText() {
        // Prepare data
        when(addReport.editTitle.getText().toString()).thenReturn("Test Title");
        when(addReport.editContent.getText().toString()).thenReturn("Test Content");
        when(addReport.editPlace.getText().toString()).thenReturn("Test Place");
        when(addReport.editTextDate.getText().toString()).thenReturn("2023-01-01");
        when(addReport.editTextTime.getText().toString()).thenReturn("12:00:00");

        // Capture the intent
        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        addReport.sendText();
        verify(addReport).startActivity(intentCaptor.capture());

        // Validate the intent
        Intent capturedIntent = intentCaptor.getValue();
        assertNotNull(capturedIntent);
        assertEquals(Intent.ACTION_SEND, capturedIntent.getAction());
        assertEquals("text/plain", capturedIntent.getType());
        assertEquals("Raport: Test Title\nSzczegóły: Test Content\nData: 2023-01-01 12:00:00\nMiejsce: Test Place\nWygenerowano w aplikacji Raporty serwisowe.",
                capturedIntent.getStringExtra(Intent.EXTRA_TEXT));
    }

    class EditableString implements Editable {
        private final String value;

        EditableString(String value) {
            this.value = value;
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public char charAt(int index) {
            return 0;
        }

        @NonNull
        @Override
        public CharSequence subSequence(int start, int end) {
            return null;
        }

        @Override
        public String toString() {
            return value;
        }

        @Override
        public Editable replace(int st, int en, CharSequence source, int start, int end) {
            return null;
        }

        @Override
        public Editable replace(int st, int en, CharSequence text) {
            return null;
        }

        @Override
        public Editable insert(int where, CharSequence text, int start, int end) {
            return null;
        }

        @Override
        public Editable insert(int where, CharSequence text) {
            return null;
        }

        @Override
        public Editable delete(int st, int en) {
            return null;
        }

        @Override
        public Editable append(CharSequence text) {
            return null;
        }

        @Override
        public Editable append(CharSequence text, int start, int end) {
            return null;
        }

        @Override
        public Editable append(char text) {
            return null;
        }

        @Override
        public void clear() {

        }

        @Override
        public void clearSpans() {

        }

        @Override
        public void setFilters(InputFilter[] filters) {

        }

        @Override
        public InputFilter[] getFilters() {
            return new InputFilter[0];
        }

        @Override
        public void getChars(int start, int end, char[] dest, int destoff) {

        }

        @Override
        public void setSpan(Object what, int start, int end, int flags) {

        }

        @Override
        public void removeSpan(Object what) {

        }

        @Override
        public <T> T[] getSpans(int start, int end, Class<T> type) {
            return null;
        }

        @Override
        public int getSpanStart(Object tag) {
            return 0;
        }

        @Override
        public int getSpanEnd(Object tag) {
            return 0;
        }

        @Override
        public int getSpanFlags(Object tag) {
            return 0;
        }

        @Override
        public int nextSpanTransition(int start, int limit, Class type) {
            return 0;
        }

        // Implement other methods from Editable as needed
        // For simplicity, these can be left unimplemented if not used in the tests
    }
}