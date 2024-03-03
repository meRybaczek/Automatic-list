//package project;
//
//import com.fazecast.jSerialComm.SerialPort;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class PortConnectionTest {
//    private final project.backend.EmployeeService employeeService = mock(project.backend.EmployeeService.class);
//    private PortConnection portConnection;
//    private SerialPort mockedComPort;
//    private StringBuilder measurements;
//
//    @BeforeEach
//    void setUp() {
//        measurements = new StringBuilder();
//        mockedComPort = Mockito.mock(SerialPort.class);
//        portConnection = new PortConnection(measurements, mockedComPort, employeeService);
//    }
//
//    @Test
//    void testGetMeasurement() {
//        //given
//        String testData = "testData";
//        String expectedString = "name surname";
//        byte[] testBytes = testData.getBytes();
//        InputStream mockedInputStream = new ByteArrayInputStream(testBytes);
//        when(mockedComPort.openPort()).thenReturn(true);
//        when(mockedComPort.getInputStream()).thenReturn(mockedInputStream);
//        when(mockedComPort.bytesAvailable()).thenReturn(testBytes.length);
//
//        //when
//        StringBuilder result = portConnection.getAuthorization();
//
//        //then
//        assertThat(result.toString()).isEqualTo(expectedString);
//    }
//}