data "local_file" "springboot" {
  filename = "${path.module}/files/springboot.yaml"
}

resource "aws_ssm_parameter" "spring-aws-ssm" {
  name        = "/components/spring-aws-ssm/itest/springboot.yaml"
  description = "Spring AWS SSM application.yaml for miljoe itest"
  type        = "SecureString"
  key_id      = aws_kms_key.spring_aws_ssm.id
  overwrite   = true

  value = data.local_file.springboot.content

  tags = {
    Name = "spring-aws-ssm-itest"
  }
}



