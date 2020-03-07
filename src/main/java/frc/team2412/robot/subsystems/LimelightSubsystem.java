package frc.team2412.robot.subsystems;

import com.robototes.sensors.Limelight;
import com.robototes.sensors.Limelight.CamMode;
import com.robototes.sensors.Limelight.LEDMode;
import com.robototes.sensors.Limelight.Pipeline;
import com.robototes.sensors.Limelight.SnapshotMode;
import com.robototes.sensors.Limelight.StreamMode;
import com.robototes.units.Rotations;
import com.robototes.units.UnitTypes.RotationUnits;

import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.HttpCamera.HttpCameraKind;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team2412.robot.subsystems.constants.ShooterConstants;
import frc.team2412.robot.subsystems.constants.ShooterConstants.ShooterDistanceDataPoint;
import frc.team2412.robot.subsystems.constants.ShooterConstants.ShooterSkewDataPoint;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

public class LimelightSubsystem extends SubsystemBase implements Loggable {

	// Store local values of distance and yaw so they aren't calculated multiple
	// times a loop
	@Log
	public ShooterDistanceDataPoint m_distanceData;
	@Log.ToString(tabName = "Turret")
	public Rotations m_yawFromTarget;
	@Log
	public ShooterSkewDataPoint m_skewData;

	@Log.CameraStream(tabName = "Turret")
	@Log.CameraStream(tabName = "Driver View")
	public HttpCamera limeCam;

	// Store the limelight
	private Limelight m_limelight;

	public LimelightSubsystem(Limelight limelight) {
		this.m_limelight = limelight;
		m_limelight.setLedMode(LEDMode.ON);
		m_limelight.setCamMode(CamMode.VISION_PROCESSER);
		m_limelight.setPipeline(Pipeline.FOUR);
		m_limelight.setSnapshotMode(SnapshotMode.OFF);
		m_limelight.setStreamMode(StreamMode.STANDARD);

		this.m_yawFromTarget = new Rotations(0);

		limeCam = getCameraStream();
	}

	public HttpCamera getCameraStream() {
		return new HttpCamera("limelight", "http://10.24.12.11:5801/stream.mjpg", HttpCameraKind.kMJPGStreamer);
	}

	public void getValues() {
		// If we have a target, set distance and yaw, otherwise error them
		if (m_limelight.hasValidTarget()) {
			setDistanceDataFromTable();
			setYawFromTable();
			setSkewDataFromTable();
		}
	}

	public void setSkewDataFromTable() {
		m_skewData = ShooterConstants.getSkewDataPointFromTs(m_limelight.getTS());
	}

	public Rotations getYawFromTarget() {
		return m_yawFromTarget;
	}

	@Override
	public void periodic() {
	}

	public void setDistanceDataFromTable() {
		m_distanceData = ShooterConstants.getDistanceDataPointFromTy(m_limelight.getTY());
	}

	public void setYawFromTable() {
		m_yawFromTarget = new Rotations(m_limelight.getTX(), RotationUnits.DEGREE);
	}

	public void stopLimelight() {
		m_limelight.setLedMode(LEDMode.OFF);
	}

	public void startLimelight() {
		m_limelight.setLedMode(LEDMode.ON);
	}

	public ShooterDistanceDataPoint getDistanceData() {
		return m_distanceData;
	}

	public ShooterSkewDataPoint getSkewData() {
		return this.m_skewData;
	}

	public boolean innerGoalPossible() {
		return m_skewData.m_innerGoalPossible;
	}
}
