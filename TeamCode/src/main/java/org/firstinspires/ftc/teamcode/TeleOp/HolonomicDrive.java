package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
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
        int lpos = 0, apos = 0;
        double xAxis = 0, yAxis = 0, zAxis = 0, position = 0.5;
        double lfPower = 0, rfPower = 0, lbPower = 0, rbPower = 0, strafePower = 0, armPower = 0, liftPower = 0;
        long htime = 0;
        boolean lControl = false, aControl = true, foundation = false;
        double lTime = 0, aTime = 0, fTime = 0;

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
            //LIFT CONTROLS TOGGLE      --FOR USE AND REVISION ONCE USING LIFT
            /*if (gamepad2.right_bumper && lTime + 500 < time.milliseconds()){
                lControl = !lControl;
                lTime = time.milliseconds();
                lpos = lift.getCurrentPosition();
            }
            //MANUAL LIFT CONTROLS
            if (lControl = true) {
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if (gamepad2.right_trigger > 0.05) {
                    liftPower = Range.clip(gamepad2.right_trigger, 0, 0.5);
                    lift.setPower(liftPower); //LIFT DOWN
                } else if (gamepad2.left_trigger > 0.05) {
                    liftPower = -Range.clip(gamepad2.right_trigger, 0, 0.5);
                    lift.setPower(liftPower); //LIFT UP
                } else {
                    liftPower = 0;
                    lift.setPower(liftPower);
                }
            }
            //POSITION LIFT CONTROLS
            else {
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lift.setPower(0.3);
                if (gamepad2.dpad_up) {
                    lpos -= 10; //LIFT DOWN
                } else if (gamepad2.left_trigger > 0.05) {
                    lpos += 10; //LIFT UP
                }
                lift.setTargetPosition(lpos);
            }*/

            //LIFT CONTROLS (incremental)   ------------------------Uncomment when we know encoder positions
            /*if (gamepad2.dpad_up) {
                double currPos = lift.getCurrentPosition();//get current position
                int tarPos = ((int)currPos/10) + 1; //find tens place and add 1 to it -----------------------------Fix values so it adjusts to the right increments (levels of skyscraper) in inches (encoders to inches)
                lift.setTargetPosition(tarPos * 10); //Make lift go to position
            }
            if (gamepad2.dpad_down) {
                double currPos = lift.getCurrentPosition();//get current position
                int tarPos = ((int)currPos/10) - 1; //find tens place and add 1 to it -----------------------------Fix values so it adjusts to the right increments (levels of skyscraper) in inches (encoders to inches)
                lift.setTargetPosition(tarPos * 10); //Make lift go to position
            }*/

            //INTAKE
            if(Math.abs(gamepad2.right_trigger) > 0.05){
                intakeL.setPower(gamepad2.right_trigger);
                intakeR.setPower(gamepad2.right_trigger);
            }
            if(Math.abs(gamepad2.left_trigger) > 0.05){
                intakeL.setPower(-gamepad2.left_trigger);
                intakeR.setPower(-gamepad2.left_trigger);
            }

            //CLAW MOVEMENT
            if (gamepad2.x){
                //setClawPosition(true); //OPEN CLAW
                claw.setPosition(1.0);
            }
            if (gamepad2.y){
                //pos -= 0.1;
                claw.setPosition(0.0); //CLOSE CLAW
            }

            if (gamepad1.right_bumper && fTime + 500 < time.milliseconds()){
                foundation = !foundation;
                fTime = time.milliseconds();
                foundationD(foundation);
            }


            //ARM MOVEMENT
            if (gamepad2.right_bumper && aTime + 500 < time.milliseconds()){
                aControl = !aControl;
                aTime = time.milliseconds();
                apos = arm.getCurrentPosition();
            }
            if (aControl){
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if (Math.abs(gamepad2.right_stick_y) > 0.05){
                    armPower = Range.clip(gamepad2.right_stick_y, -0.2, 0.2);
                    arm.setPower(-armPower);
                }else{
                    arm.setPower(0);
                }
            }
            else{
                arm.setPower(0.3);
                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                if (gamepad2.dpad_up){
                    apos += 10;
                }
                if (gamepad2.dpad_down){
                    apos -= 10;
                }
                arm.setTargetPosition(apos);
            }


            /*//CLAW ROTATE
            if (gamepad2.dpad_left){
                rotate.setPosition(0.6);
            }else if(gamepad2.dpad_right){
                rotate.setPosition(0.4);
            }
            else{
                rotate.setPosition(0.5);
            }

            //IF WE DECIDE TO HAVE INCREMENTAL ROTATE INSTEAD OF CONTINUOUS
            if (gamepad2.dpad_left){
                //position += 0.1;
                rotate.setPosition(0.95);
            }else if(gamepad2.dpad_right){
                //position -= 0.1;
                rotate.setPosition(0.5);
            }*/

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
                zAxis = -gamepad1.right_stick_x;
            }
            else{
                zAxis = 0;
            }

            //HALFSPEED (toggle)
            if (gamepad1.x) {
                htime = time.now(TimeUnit.SECONDS);
                halfSpeed = !halfSpeed;
            }

            if (gamepad1.right_trigger > 0.05){
                strafePower = gamepad1.right_trigger * 0.75;
                setStrafePowers(strafePower,true);
            }else if (gamepad1.left_trigger > 0.05) {
                strafePower = gamepad1.left_trigger * 0.75;
                setStrafePowers(strafePower, false);
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
            telemetry.addData("arm Power", armPower);
            telemetry.addData("arm encoder", arm.getCurrentPosition());
            telemetry.addData("lift encoder", lift.getCurrentPosition());
            telemetry.addData("lift power", liftPower);
            telemetry.addData("claw position", claw.getPosition());
            //telemetry.addData("rotate position", rotate.getPosition());
            telemetry.update();
        }
    }
}
