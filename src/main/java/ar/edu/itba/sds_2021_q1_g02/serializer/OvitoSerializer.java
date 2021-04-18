package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Position;
import ar.edu.itba.sds_2021_q1_g02.models.Velocity;

import java.io.*;
import java.util.List;

public class OvitoSerializer implements Serializer {
    private final ParticleFormatter particleFormatter;
    private final FileFormatter fileFormatter;
    private final Dimen systemDimen;

    public OvitoSerializer(ParticleFormatter particleFormatter, FileFormatter fileFormatter, Dimen systemDimen) {
        this.particleFormatter = particleFormatter;
        this.fileFormatter = fileFormatter;
        this.systemDimen = systemDimen;
    }

    @Override
    public void serialize(List<Particle> particles, int step, double dt) {
        File file = new File(this.fileFormatter.formatFilename(step));
        if (file.exists() && !file.delete())
            throw new RuntimeException("Couldn't delete file: " + file.getName());

        try {
            if (!file.createNewFile())
                throw new RuntimeException("Couldn't create file: " + file.getName());

            FileWriter writer = new FileWriter(file);

            writer.write(this.fileFormatter.formatSystem(particles));
            writer.write("\n");
            writer.write(this.particleFormatter.format(new Particle(-2, 0, 0, new Position(this.systemDimen.getXvf(), this.systemDimen.getYvi()), new Velocity(0, 0)), step, dt));
            writer.write("\n");
            writer.write(this.particleFormatter.format(new Particle(-1, 0, 0, new Position(this.systemDimen.getXvi(), this.systemDimen.getYvf()), new Velocity(0, 0)), step, dt));
            writer.write("\n");
            for (Particle p : particles) {
                writer.write(this.particleFormatter.format(p, step, dt));
                writer.write("\n");
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}