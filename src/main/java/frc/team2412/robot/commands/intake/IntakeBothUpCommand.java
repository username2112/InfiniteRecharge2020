package frc.team2412.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.team2412.robot.subsystems.IntakeUpDownSubsystem;

public class IntakeBothUpCommand extends ParallelCommandGroup {

	private IntakeUpDownSubsystem m_intakeUpDownSubsystem;

	public IntakeBothUpCommand(IntakeUpDownSubsystem intakeUpDownSubsystem) {
		addRequirements(intakeUpDownSubsystem);
		m_intakeUpDownSubsystem = intakeUpDownSubsystem;
	}

	@Override
	public void execute() {
		m_intakeUpDownSubsystem.frontIntakeUp();
		m_intakeUpDownSubsystem.backIntakeUp();
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}