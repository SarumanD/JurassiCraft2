package org.jurassicraft.server.entity.ai;

import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.core.Mutex;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DinosaurWanderEntityAI extends EntityAIBase
{
    protected DinosaurEntity entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    protected int executionChance;
    private boolean mustUpdate;


    public DinosaurWanderEntityAI(DinosaurEntity creatureIn, double speedIn, int chance)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!this.mustUpdate)
        {
            if (innerShouldExecute())
            {
                return false;
            }
        }

        if (this.outterShouldExecute())
        {
            overlist:
                for(int i = 0; i < 100; i++) {
                    Vec3d vec = getWanderPosition();
                    if (vec != null) {
                        for(BlockPos pos : BlockPos.getAllInBox(new BlockPos(vec.addVector(0, 1, 0)), new BlockPos(vec.addVector(1, 1, 1)))) {
                            if(this.entity.world.getBlockState(pos).getMaterial() != Material.AIR) {
                        	continue overlist;
                            }
                        }
                        this.xPosition = vec.x;
                        this.yPosition = vec.y;
                        this.zPosition = vec.z;
                        this.mustUpdate = false;
                        return true;
                    }
                }
        }

        return false;
    }
    
    protected boolean innerShouldExecute() { //TODO: merge into one
	return this.entity.getRNG().nextInt(this.executionChance) != 0;
    }
    
    protected boolean outterShouldExecute() {
	return this.entity.getNavigator().noPath() && this.entity.getAttackTarget() == null;
    }
    
    protected Vec3d getWanderPosition() {
        return RandomPositionGenerator.getLandPos(this.entity, 10, 10);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return !this.entity.getNavigator().noPath() & !this.entity.isInWater();
    }

    @Override
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate()
    {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int chance)
    {
        this.executionChance = chance;
    }
}