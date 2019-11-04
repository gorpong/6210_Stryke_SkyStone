package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SkystoneLinearOpMode;

import java.util.concurrent.TimeUnit;

@TeleOp(name="(->)HolonomicDrive", group="teleop")
//@Disabled
public class HolonomicDrive extends SkystoneLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, false);

        double xAxis = 0, yAxis = 0, zAxis = 0, position = 0.5;
        double lfPower = 0, rfPower = 0, lbPower = 0, rbPower = 0, strafePower = 0;
        long htime = 0, rtime = 0;


        //For more controlled movement when moving the foundation
        boolean halfSpeed = false;
        resetEncoders();
        resetArm();

        //Set up a timer for half speed
        ElapsedTime time = new ElapsedTime();
        resetTime();

        telemetry.addData("Mode: ", "Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            //LIFT CONTROLS
            if (gamepad2.right_trigger > 0.05) {
                lift.setPower(gamepad2.right_trigger); //LIFT DOWN
            }else if(gamepad2.left_trigger > 0.05){
                lift.setPower(-gamepad2.left_trigger); //LIFT UP
            }else{
                lift.setPower(0);
            }

            //LIFT CONTROLS (incremental)
            if (gamepad2.dpad_up) {
                double currPos = lift.getCurrentPosition();//get current position
                int tarPos = ((int)currPos/10) + 1; //find tens place and add 1 to it -----------------------------Fix values so it adjusts to the right increments (levels of skyscraper) in inches (encoders to inches)
                lift.setTargetPosition(tarPos * 10); //Make lift go to position
            }
            if (gamepad2.dpad_down) {
                double currPos = lift.getCurrentPosition();//get current position
                int tarPos = ((int)currPos/10) - 1; //find tens place and add 1 to it -----------------------------Fix values so it adjusts to the right increments (levels of skyscraper) in inches (encoders to inches)
                lift.setTargetPosition(tarPos * 10); //Make lift go to position
            }

            //CLAW MOVEMENT
            if (gamepad2.b){
                setClawPosition(true); //OPEN CLAW
            }
            if (gamepad2.a){
                setClawPosition(false); //CLOSE CLAW
            }

            //ARM MOVEMENT
            if (Math.abs(gamepad2.right_stick_y) > 0.05){
                arm.setPower(Range.clip(gamepad2.right_stick_y, 0.0, 0.5));
            }else{
                arm.setPower(0);
            }

            //CLAW ROTATE
            if (gamepad2.dpad_left && rtime + 50 < time.now(TimeUnit.MILLISECONDS)){
                rtime = time.now(TimeUnit.MILLISECONDS);
                position += 0.5;
                rotate.setPosition(Range.clip(position, 0, 1));
            }else if(gamepad2.dpad_right && rtime + 50 < time.now(TimeUnit.MILLISECONDS)){
                rtime = time.now(TimeUnit.MILLISECONDS);
                position -= 0.25;
                rotate.setPosition(Range.clip(position, 0, 1));
            }

            //HOLONOMIC DRIVE
            if (Math.abs(gamepad1.left_stick_y) > 0.05) {
                yAxis = gamepad1.left_stick_y;
            }
            else{
                yAxis = 0;
            }
            if (Math.abs(gamepad1.left_stick_x) > 0.05) {
                xAxis = -gamepad1.left_stick_x;
            }
            else{
                xAxis = 0;
            }
            if (Math.abs(gamepad1.right_stick_x) > 0.05) {
                zAxis = gamepad1.right_stick_x;
            }
            else{
                zAxis = 0;
            }

            //HALFSPEED (toggle)
            if (gamepad1.x && htime + 1 > time.now(TimeUnit.SECONDS)) {
                htime = time.now(TimeUnit.SECONDS);
                halfSpeed = !halfSpeed;
            }

            lfPower = yAxis+xAxis-zAxis;
            rfPower = yAxis-xAxis+zAxis;
            lbPower = yAxis-xAxis-zAxis;
            rbPower = yAxis+xAxis+zAxis;

            if (gamepad1.left_trigger > 0.05){
                strafePower = gamepad1.left_trigger * 0.75;
                setStrafePowers(strafePower,false);
            }else if (gamepad1.right_trigger > 0.05) {
                strafePower = gamepad1.right_trigger * 0.75;
                setStrafePowers(strafePower, true);
            }else if (halfSpeed){
                lfPower = ((yAxis+xAxis-zAxis)/2);
                rfPower = ((yAxis-xAxis+zAxis)/2);
                lbPower = ((yAxis-xAxis-zAxis)/2);
                rbPower = ((yAxis+xAxis+zAxis)/2);

                LF.setPower(Range.clip(lfPower, -0.5, 0.5));
                RF.setPower(Range.clip(rfPower, -0.5, 0.5));
                LB.setPower(Range.clip(lbPower, -0.5, 0.5));
                RB.setPower(Range.clip(rbPower, -0.5, 0.5));
            }else {
                lfPower = yAxis + xAxis - zAxis;
                rfPower = yAxis - xAxis + zAxis;
                lbPower = yAxis - xAxis - zAxis;
                rbPower = yAxis + xAxis + zAxis;

                LF.setPower(Range.clip(lfPower, -1, 1));
                RF.setPower(Range.clip(rfPower, -1, 1));
                LB.setPower(Range.clip(lbPower, -1, 1));
                RB.setPower(Range.clip(rbPower, -1, 1));
            }
            
            telemetry.addData("Y Axis", yAxis);
            telemetry.addData("X Axis", xAxis);
            telemetry.addData("Z Axis", zAxis);
            telemetry.addData("LF Power", lfPower);
            telemetry.addData("RF Power", rfPower);
            telemetry.addData("LB Power", lbPower);
            telemetry.addData("RB Power", rbPower);
            telemetry.addData("strafe Power", strafePower);
            telemetry.addData("arm encoder", arm.getCurrentPosition());
            telemetry.addData("claw position", claw.getPosition());
            telemetry.addData("rotate position", rotate.getPosition());
            telemetry.update();
        }
    }
}
