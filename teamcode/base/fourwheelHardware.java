package org.firstinspires.ftc.teamcode.base;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Bob on 11/3/2017.
 *
 * In OpMode:
 *      fourwheelHardware robot = new fourwheelHardware();
 *   and
 *      robot.init(hardwareMap);
 */

/**
 * Created by 22173485 on 12/11/2017.
 */

public class fourwheelHardware
{

    private DcMotor drive_front_left;
    private DcMotor drive_front_right;
    private DcMotor drive_back_left;
    private DcMotor drive_back_right;

    private Servo armServo;
    private Servo jewelArm;

    private static final double ARM_RETRACTED_POSITION = 0.2;
    private static final double ARM_EXTENDED_POSITION = 0.8;
    private static final double JEWEL_ARM_RETRACTED_POSITION = 0.6;
    private static final double JEWEL_ARM_EXTENDED_POSITION = 0.1;

    HardwareMap hwMap           =  null;

    public fourwheelHardware()
    {

    }

    public void init(HardwareMap ahwMap)
    {
        // copy reference to Hardare Map
        hwMap = ahwMap;

        drive_front_left = hwMap.get(DcMotor.class, "DRIVE_FRONT_LEFT");
        drive_front_right = hwMap.get(DcMotor.class, "DRIVE_FRONT_RIGHT");
        drive_back_left = hwMap.get(DcMotor.class, "DRIVE_BACK_LEFT");
        drive_back_right = hwMap.get(DcMotor.class, "DRIVE_BACK_RIGHT");

        drive_front_left.setDirection(DcMotor.Direction.REVERSE);
        drive_back_left.setDirection(DcMotor.Direction.REVERSE);

        //  set wheel motors to zero power
        drive_front_left.setPower(0);
        drive_front_right.setPower(0);
        drive_back_left.setPower(0);
        drive_back_right.setPower(0);

        // Set all motors to run without encoders.

        // Define and initialize ALL installed servos.
        armServo  = hwMap.get(Servo.class, "armServo");
        jewelArm  = hwMap.get(Servo.class, "jewelArm");

        armServo.setPosition(ARM_RETRACTED_POSITION);
        jewelArm.setPosition(JEWEL_ARM_RETRACTED_POSITION);

/*
    Copied from HardwarePushbot class
    Use what is needed

            // Set all motors to zero power
    leftDrive.setPower(0);
    rightDrive.setPower(0);
    leftArm.setPower(0);

    // Set all motors to run without encoders.
    // May want to use RUN_USING_ENCODERS if encoders are installed.
    leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    leftArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    // Define and initialize ALL installed servos.
    leftClaw  = hwMap.get(Servo.class, "left_hand");
    rightClaw = hwMap.get(Servo.class, "right_hand");
    leftClaw.setPosition(MID_SERVO);
    rightClaw.setPosition(MID_SERVO);

*/
    }
//    }


}
